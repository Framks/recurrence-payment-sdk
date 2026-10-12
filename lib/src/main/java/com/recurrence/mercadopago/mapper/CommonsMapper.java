package com.recurrence.mercadopago.mapper;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.PlanInterval;
import com.recurrence.contract.domain.enums.Currency;
import com.recurrence.contract.domain.enums.InvoiceStatus;
import com.recurrence.contract.domain.enums.PaymentMethodType;
import com.recurrence.contract.domain.enums.PaymentStatus;
import com.recurrence.contract.domain.enums.PlanIntervalType;
import com.recurrence.contract.domain.enums.PlanStatus;
import com.recurrence.contract.domain.enums.SubscriptionStatus;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.exception.RecurrenceSerializationException;
import com.recurrence.mercadopago.domain.commons.MercadoPagoFreeTrial;
import com.recurrence.mercadopago.domain.commons.MercadoPagoIdRef;
import com.recurrence.mercadopago.domain.commons.MercadoPagoPaging;
import com.recurrence.mercadopago.domain.plan.MercadoPagoPaymentMethodsAllowed;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Function;

/**
 * Conversões compartilhadas entre os mappers do Mercado Pago.
 *
 * <p>Valores do Mercado Pago sem equivalente canônico viram {@code UNKNOWN} em
 * vez de erro, para que um estado novo não quebre a leitura.</p>
 */
final class CommonsMapper {

    private static final String CREDIT_CARD = "credit_card";
    private static final String DAYS = "days";
    private static final String MONTHS = "months";
    private static final int DAYS_PER_MONTH = 30;

    private CommonsMapper() { }

    // ---------- valores: contrato em centavos, Mercado Pago em decimal ----------

    static BigDecimal toDecimal(Integer cents) {
        if (cents == null) return null;
        return BigDecimal.valueOf(cents).movePointLeft(2);
    }

    static Integer toCents(BigDecimal value) {
        if (value == null) return null;
        return value.movePointRight(2).setScale(0, RoundingMode.HALF_UP).intValueExact();
    }

    static String toCurrencyId(Amount amount) {
        return amount == null || amount.currency() == null ? null : amount.currency().name();
    }

    static Amount toContract(BigDecimal value, String currencyId) {
        if (value == null) return null;
        return new Amount(toCents(value), toCurrency(currencyId));
    }

    private static Currency toCurrency(String currency) {
        if (currency == null) return null;
        for (Currency value : Currency.values()) {
            if (value.name().equalsIgnoreCase(currency)) return value;
        }
        return null;
    }

    // ---------- intervalo e período de teste ----------

    static String toFrequencyType(PlanIntervalType type) {
        if (type == null) return null;
        return switch (type) {
            case DAY -> DAYS;
            case MONTH -> MONTHS;
        };
    }

    static PlanInterval toContractInterval(Integer frequency, String frequencyType) {
        if (frequency == null || frequencyType == null) return null;
        return switch (frequencyType) {
            case DAYS -> new PlanInterval(PlanIntervalType.DAY, frequency);
            case MONTHS -> new PlanInterval(PlanIntervalType.MONTH, frequency);
            default -> null;
        };
    }

    static MercadoPagoFreeTrial toFreeTrial(Integer days) {
        if (days == null) return null;
        return new MercadoPagoFreeTrial(days, DAYS);
    }

    /** O contrato expressa o teste em dias; teste em meses vira {@code meses x 30}. */
    static Integer toTrialDays(MercadoPagoFreeTrial freeTrial) {
        if (freeTrial == null || freeTrial.frequency() == null) return null;
        return MONTHS.equals(freeTrial.frequencyType())
                ? freeTrial.frequency() * DAYS_PER_MONTH
                : freeTrial.frequency();
    }

    // ---------- meio de pagamento ----------

    static PaymentMethodType toPaymentMethodType(String paymentTypeId) {
        return CREDIT_CARD.equals(paymentTypeId) ? PaymentMethodType.CREDIT_CARD : PaymentMethodType.UNKNOWN;
    }

    static MercadoPagoPaymentMethodsAllowed toPaymentMethodsAllowed(List<PaymentMethodType> types) {
        if (types == null) return null;
        return new MercadoPagoPaymentMethodsAllowed(
                types.stream().map(type -> new MercadoPagoIdRef(CREDIT_CARD)).distinct().toList());
    }

    static List<PaymentMethodType> toContractPaymentMethods(MercadoPagoPaymentMethodsAllowed allowed) {
        if (allowed == null || allowed.paymentTypes() == null) return null;
        return allowed.paymentTypes().stream()
                .map(type -> toPaymentMethodType(type.id()))
                .toList();
    }

    // ---------- status ----------

    /** {@code inactive} é convertido em {@code cancelled} pelo Mercado Pago; ambos são reversíveis. */
    static PlanStatus toPlanStatus(String status) {
        if (status == null) return null;
        return switch (status) {
            case "active" -> PlanStatus.ACTIVE;
            case "inactive", "cancelled" -> PlanStatus.INACTIVE;
            default -> PlanStatus.UNKNOWN;
        };
    }

    static SubscriptionStatus toSubscriptionStatus(String status) {
        if (status == null) return null;
        return switch (status) {
            case "authorized" -> SubscriptionStatus.ACTIVE;
            case "pending" -> SubscriptionStatus.PENDING;
            case "paused" -> SubscriptionStatus.SUSPENDED;
            case "cancelled" -> SubscriptionStatus.CANCELED;
            case "finished" -> SubscriptionStatus.EXPIRED;
            default -> SubscriptionStatus.UNKNOWN;
        };
    }

    /**
     * A fatura ({@code authorized_payment}) só está paga quando foi processada e o
     * pagamento vinculado foi aprovado; agendada, em retentativa ({@code recycling})
     * ou processada sem aprovação conta como não paga.
     */
    static InvoiceStatus toInvoiceStatus(String status, String paymentStatus) {
        if (status == null) return null;
        return switch (status) {
            case "processed" -> "approved".equals(paymentStatus) ? InvoiceStatus.PAID : InvoiceStatus.UNPAID;
            case "scheduled", "recycling" -> InvoiceStatus.UNPAID;
            default -> InvoiceStatus.UNKNOWN;
        };
    }

    static PaymentStatus toPaymentStatus(String status) {
        if (status == null) return null;
        return switch (status) {
            case "approved" -> PaymentStatus.APPROVED;
            case "pending", "in_process", "authorized" -> PaymentStatus.PENDING;
            case "rejected" -> PaymentStatus.DECLINED;
            case "refunded", "charged_back" -> PaymentStatus.REFUNDED;
            default -> PaymentStatus.UNKNOWN;
        };
    }

    // ---------- datas e páginas ----------

    /** Aceita {@code yyyy-MM-dd} ou data/hora ISO, mantendo só a data. */
    static LocalDate toLocalDate(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDate.parse(value.length() > 10 ? value.substring(0, 10) : value);
        } catch (DateTimeParseException e) {
            throw new RecurrenceSerializationException("Mercado Pago returned an invalid date: " + value, e);
        }
    }

    static <P, T> PageResponse<T> toPage(MercadoPagoPaging paging, List<P> items, Function<P, T> mapper) {
        List<T> data = items == null ? List.of() : items.stream().map(mapper).toList();
        PageResponse.PageInfo page = paging == null
                ? new PageResponse.PageInfo(0, data.size(), data.size())
                : new PageResponse.PageInfo(
                        valueOrZero(paging.offset()),
                        valueOrZero(paging.limit()),
                        valueOrZero(paging.total()));
        return new PageResponse<>(page, data);
    }

    private static int valueOrZero(Integer value) {
        return value == null ? 0 : value;
    }
}
