package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.enums.PaymentStatus;
import com.recurrence.exception.RecurrenceValidationException;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static com.recurrence.constants.ValidationsMessage.FILTER_STATUS_UNKNOWN;

/**
 * Converte valores de filtro do contrato para parâmetros de query do PagBank.
 */
public final class PagBankFilterMapper {

    private PagBankFilterMapper() {}

    /**
     * Os nomes dos status canônicos coincidem com os do PagBank.
     * {@code UNKNOWN} não é um estado real e não pode ser filtrado.
     */
    public static String status(Enum<?> status) {
        if (status == null) return null;
        if ("UNKNOWN".equals(status.name())) {
            throw new RecurrenceValidationException(FILTER_STATUS_UNKNOWN);
        }
        return status.name();
    }

    /** Pagamento recusado é {@code DENIED} no PagBank; os demais coincidem. */
    public static String paymentStatus(PaymentStatus status) {
        if (status == PaymentStatus.DECLINED) return "DENIED";
        return status(status);
    }

    /** O PagBank filtra por data ({@code yyyy-MM-dd}). */
    public static String date(OffsetDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
