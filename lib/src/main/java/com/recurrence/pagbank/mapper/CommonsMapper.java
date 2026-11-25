package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.commons.CustomerReference;
import com.recurrence.contract.domain.commons.PlanInterval;
import com.recurrence.contract.domain.commons.PlanReference;
import com.recurrence.contract.domain.enums.Currency;
import com.recurrence.contract.domain.enums.InvoiceStatus;
import com.recurrence.contract.domain.enums.PaymentMethodType;
import com.recurrence.contract.domain.enums.PaymentStatus;
import com.recurrence.contract.domain.enums.PlanIntervalType;
import com.recurrence.contract.domain.enums.PlanStatus;
import com.recurrence.contract.domain.enums.SubscriptionStatus;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.exception.RecurrenceSerializationException;
import com.recurrence.pagbank.domain.commons.PagBankAmount;
import com.recurrence.pagbank.domain.commons.PagBankBillingInfo;
import com.recurrence.pagbank.domain.commons.PagBankCard;
import com.recurrence.pagbank.domain.commons.ResultSet;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import com.recurrence.pagbank.domain.plan.PagBankPlan;
import com.recurrence.pagbank.domain.plan.PagBankPlanInterval;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Function;

/**
 * Conversões compartilhadas entre os mappers do PagBank.
 *
 * <p>Valores do PagBank sem equivalente canônico viram {@code UNKNOWN} em vez
 * de erro, para que um estado novo não quebre a leitura.</p>
 */
final class CommonsMapper {

    private static final String CREDIT_CARD = "CREDIT_CARD";

    private CommonsMapper() { }

    // ---------- valores ----------

    static PagBankAmount toPagBank(Amount amount) {
        if (amount == null) return null;
        return new PagBankAmount(amount.value(), amount.currency().name());
    }

    static Amount toContract(PagBankAmount amount) {
        if (amount == null) return null;
        return new Amount(amount.value(), toCurrency(amount.currency()));
    }

    private static Currency toCurrency(String currency) {
        if (currency == null) return null;
        for (Currency value : Currency.values()) {
            if (value.name().equalsIgnoreCase(currency)) return value;
        }
        return null;
    }

    // ---------- intervalo ----------

    static PagBankPlanInterval toPagBank(PlanInterval interval) {
        if (interval == null) return null;
        return new PagBankPlanInterval(interval.frequency(), interval.type().name());
    }

    /** PagBank aceita {@code YEAR}; convertido sem perda para {@code MONTH x 12}. */
    static PlanInterval toContract(PagBankPlanInterval interval) {
        if (interval == null || interval.unit() == null || interval.length() == null) return null;
        return switch (interval.unit()) {
            case "DAY" -> new PlanInterval(PlanIntervalType.DAY, interval.length());
            case "MONTH" -> new PlanInterval(PlanIntervalType.MONTH, interval.length());
            case "YEAR" -> new PlanInterval(PlanIntervalType.MONTH, interval.length() * 12);
            default -> null;
        };
    }

    // ---------- meio de pagamento ----------

    static PaymentMethodType toPaymentMethodType(String type) {
        return CREDIT_CARD.equals(type) ? PaymentMethodType.CREDIT_CARD : PaymentMethodType.UNKNOWN;
    }

    static List<String> toPagBankPaymentMethods(List<PaymentMethodType> types) {
        if (types == null) return null;
        return types.stream().map(Enum::name).toList();
    }

    static List<PaymentMethodType> toContractPaymentMethods(List<String> types) {
        if (types == null) return null;
        return types.stream().map(CommonsMapper::toPaymentMethodType).toList();
    }

    /** Cartão completo, para {@code customer.billing_info}. */
    static PagBankBillingInfo toPagBankBillingInfo(BillingInfo billingInfo) {
        return new PagBankBillingInfo(
                CREDIT_CARD,
                PagBankCard.encrypted(billingInfo.getCard().getToken(), billingInfo.getCard().getSecurityCode())
        );
    }

    /** Só o CVV, para {@code subscription.payment_method}. */
    static PagBankBillingInfo toPagBankPaymentMethod(BillingInfo billingInfo) {
        return new PagBankBillingInfo(
                CREDIT_CARD,
                PagBankCard.securityCode(billingInfo.getCard().getSecurityCode())
        );
    }

    static BillingInfo toContract(PagBankBillingInfo billingInfo) {
        if (billingInfo == null) return null;
        Card card = null;
        if (billingInfo.card() != null) {
            card = Card.builder()
                    .brand(billingInfo.card().brand())
                    .lastDigits(billingInfo.card().lastDigits())
                    .build();
        }
        return BillingInfo.builder()
                .type(toPaymentMethodType(billingInfo.type()))
                .card(card)
                .build();
    }

    static BillingInfo firstBillingInfo(List<PagBankBillingInfo> billingInfos) {
        if (billingInfos == null || billingInfos.isEmpty()) return null;
        return toContract(billingInfos.get(0));
    }

    // ---------- referências ----------

    static PlanReference toPlanReference(PagBankPlan plan) {
        if (plan == null) return null;
        return new PlanReference(plan.id(), plan.name());
    }

    static CustomerReference toCustomerReference(PagBankCustomer customer) {
        if (customer == null) return null;
        return new CustomerReference(customer.id(), customer.name(), customer.email());
    }

    // ---------- status ----------

    static PlanStatus toPlanStatus(String status) {
        if (status == null) return null;
        return switch (status) {
            case "ACTIVE" -> PlanStatus.ACTIVE;
            case "INACTIVE" -> PlanStatus.INACTIVE;
            default -> PlanStatus.UNKNOWN;
        };
    }

    /** {@code TRIAL} é uma assinatura ativa em período de teste; {@code PENDING_ACTION} aguarda ação do assinante. */
    static SubscriptionStatus toSubscriptionStatus(String status) {
        if (status == null) return null;
        return switch (status) {
            case "ACTIVE", "TRIAL" -> SubscriptionStatus.ACTIVE;
            case "CANCELED" -> SubscriptionStatus.CANCELED;
            case "OVERDUE" -> SubscriptionStatus.OVERDUE;
            case "PENDING", "PENDING_ACTION" -> SubscriptionStatus.PENDING;
            case "SUSPENDED" -> SubscriptionStatus.SUSPENDED;
            case "EXPIRED" -> SubscriptionStatus.EXPIRED;
            default -> SubscriptionStatus.UNKNOWN;
        };
    }

    /**
     * Valores observados no sandbox: {@code PAID}, {@code UNPAID} e {@code OVERDUE}
     * (cobrança recusada, logo após a tentativa). O contrato não distingue fatura
     * vencida, então {@code OVERDUE} vira {@code UNPAID}.
     */
    static InvoiceStatus toInvoiceStatus(String status) {
        if (status == null) return null;
        return switch (status) {
            case "PAID" -> InvoiceStatus.PAID;
            case "UNPAID", "OVERDUE" -> InvoiceStatus.UNPAID;
            default -> InvoiceStatus.UNKNOWN;
        };
    }

    /** Valores observados no sandbox: {@code APPROVED}, {@code DENIED} e {@code UNPAID}. */
    static PaymentStatus toPaymentStatus(String status) {
        if (status == null) return null;
        return switch (status) {
            case "APPROVED" -> PaymentStatus.APPROVED;
            case "DENIED" -> PaymentStatus.DECLINED;
            case "UNPAID" -> PaymentStatus.UNPAID;
            case "PENDING" -> PaymentStatus.PENDING;
            case "REFUNDED" -> PaymentStatus.REFUNDED;
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
            throw new RecurrenceSerializationException("PagBank returned an invalid date: " + value, e);
        }
    }

    static <P, T> PageResponse<T> toPage(ResultSet resultSet, List<P> items, Function<P, T> mapper) {
        List<T> data = items == null ? List.of() : items.stream().map(mapper).toList();
        PageResponse.PageInfo page = resultSet == null
                ? new PageResponse.PageInfo(0, data.size(), data.size())
                : new PageResponse.PageInfo(
                        valueOrZero(resultSet.offset()),
                        valueOrZero(resultSet.limit()),
                        valueOrZero(resultSet.total()));
        return new PageResponse<>(page, data);
    }

    private static int valueOrZero(Integer value) {
        return value == null ? 0 : value;
    }
}
