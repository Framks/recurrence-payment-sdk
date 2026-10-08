package com.recurrence.mercadopago.mapper;

import com.recurrence.contract.domain.enums.PaymentStatus;
import com.recurrence.contract.domain.enums.PlanStatus;
import com.recurrence.contract.domain.enums.SubscriptionStatus;
import com.recurrence.exception.RecurrenceValidationException;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static com.recurrence.constants.ValidationsMessage.FILTER_STATUS_UNKNOWN;
import static com.recurrence.constants.ValidationsMessage.FILTER_STATUS_UNSUPPORTED;

/**
 * Converte valores de filtro do contrato para parâmetros de busca do Mercado Pago.
 *
 * <p>Status canônicos sem equivalente no Mercado Pago são recusados antes da
 * chamada, em vez de devolver uma busca vazia que pareceria verdadeira.</p>
 */
public final class MercadoPagoFilterMapper {

    private static final String PROVIDER = "Mercado Pago";
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private MercadoPagoFilterMapper() {}

    public static String planStatus(PlanStatus status) {
        if (status == null) return null;
        return switch (status) {
            case ACTIVE -> "active";
            case INACTIVE -> "cancelled";
            case UNKNOWN -> throw new RecurrenceValidationException(FILTER_STATUS_UNKNOWN);
        };
    }

    /** {@code OVERDUE} e {@code EXPIRED} só existem no PagBank. */
    public static String subscriptionStatus(SubscriptionStatus status) {
        if (status == null) return null;
        return switch (status) {
            case ACTIVE -> "authorized";
            case PENDING -> "pending";
            case SUSPENDED -> "paused";
            case CANCELED -> "cancelled";
            case OVERDUE, EXPIRED -> throw unsupported(status);
            case UNKNOWN -> throw new RecurrenceValidationException(FILTER_STATUS_UNKNOWN);
        };
    }

    /** {@code UNPAID} não tem estado de pagamento equivalente no Mercado Pago. */
    public static String paymentStatus(PaymentStatus status) {
        if (status == null) return null;
        return switch (status) {
            case APPROVED -> "approved";
            case PENDING -> "pending";
            case DECLINED -> "rejected";
            case REFUNDED -> "refunded";
            case UNPAID -> throw unsupported(status);
            case UNKNOWN -> throw new RecurrenceValidationException(FILTER_STATUS_UNKNOWN);
        };
    }

    /** Formato de {@code begin_date}/{@code end_date} da busca de pagamentos. */
    public static String dateTime(OffsetDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DATE_TIME);
    }

    private static RecurrenceValidationException unsupported(Enum<?> status) {
        return new RecurrenceValidationException(String.format(FILTER_STATUS_UNSUPPORTED, status.name(), PROVIDER));
    }
}
