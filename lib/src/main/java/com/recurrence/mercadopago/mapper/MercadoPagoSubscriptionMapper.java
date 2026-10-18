package com.recurrence.mercadopago.mapper;

import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.commons.CustomerReference;
import com.recurrence.contract.domain.commons.PlanReference;
import com.recurrence.contract.domain.enums.PaymentMethodType;
import com.recurrence.contract.domain.request.CreateSubscriptionRequest;
import com.recurrence.contract.domain.request.UpdateSubscriptionRequest;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.mercadopago.domain.commons.MercadoPagoAutoRecurring;
import com.recurrence.mercadopago.domain.subscription.MercadoPagoSubscription;
import com.recurrence.mercadopago.domain.subscription.MercadoPagoSubscriptionList;
import com.recurrence.mercadopago.domain.subscription.MercadoPagoSubscriptionRequest;
import com.recurrence.mercadopago.domain.subscription.MercadoPagoSubscriptionUpdateRequest;

import static com.recurrence.mercadopago.constant.Constant.Status.SUBSCRIPTION_AUTHORIZED;

/**
 * Assinatura do contrato x {@code preapproval} do Mercado Pago.
 *
 * <p>O Mercado Pago identifica o assinante só pelo e-mail ({@code payer_email});
 * nome, CPF e telefone do {@code CustomerRequest} não são enviados.</p>
 */
public final class MercadoPagoSubscriptionMapper {

    private MercadoPagoSubscriptionMapper() {}

    public static MercadoPagoSubscriptionRequest toRequest(CreateSubscriptionRequest subscription, String backUrl) {
        return new MercadoPagoSubscriptionRequest(
                subscription.getPlanId(),
                subscription.getReferenceId(),
                subscription.getCustomer().getEmail(),
                subscription.getPaymentMethod().getCard().getToken(),
                toAmountOverride(subscription.getAmount()),
                backUrl,
                SUBSCRIPTION_AUTHORIZED
        );
    }

    public static MercadoPagoSubscriptionUpdateRequest toRequest(UpdateSubscriptionRequest subscription) {
        BillingInfo paymentMethod = subscription.getPaymentMethod();
        return new MercadoPagoSubscriptionUpdateRequest(
                subscription.getPlanId(),
                paymentMethod == null ? null : paymentMethod.getCard().getToken(),
                toAmountOverride(subscription.getAmount())
        );
    }

    public static Subscription toContract(MercadoPagoSubscription subscription) {
        MercadoPagoAutoRecurring recurring = subscription.autoRecurring();
        return Subscription.builder()
                .id(subscription.id())
                .referenceId(subscription.externalReference())
                .status(CommonsMapper.toSubscriptionStatus(subscription.status()))
                .amount(recurring == null ? null : CommonsMapper.toContract(recurring.transactionAmount(), recurring.currencyId()))
                .plan(subscription.preapprovalPlanId() == null
                        ? null
                        : new PlanReference(subscription.preapprovalPlanId(), subscription.reason()))
                .customer(new CustomerReference(subscription.payerId(), null, blankToNull(subscription.payerEmail())))
                .paymentMethod(toBillingInfo(subscription))
                .nextInvoiceAt(CommonsMapper.toLocalDate(subscription.nextPaymentDate()))
                .expAt(recurring == null ? null : CommonsMapper.toLocalDate(recurring.endDate()))
                .createdAt(subscription.dateCreated())
                .updatedAt(subscription.lastModified())
                .build();
    }

    public static PageResponse<Subscription> toPage(MercadoPagoSubscriptionList list) {
        return CommonsMapper.toPage(list.paging(), list.results(), MercadoPagoSubscriptionMapper::toContract);
    }

    /** O Mercado Pago devolve {@code payer_email} vazio nas consultas da assinatura. */
    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    /** Só o valor e a moeda; frequência continua sendo a do plano. */
    private static MercadoPagoAutoRecurring toAmountOverride(Amount amount) {
        if (amount == null) return null;
        return MercadoPagoAutoRecurring.of(
                null, null, null,
                CommonsMapper.toDecimal(amount.value()),
                CommonsMapper.toCurrencyId(amount),
                null
        );
    }

    /** O Mercado Pago informa só a bandeira ({@code payment_method_id}) e o id do cartão salvo. */
    private static BillingInfo toBillingInfo(MercadoPagoSubscription subscription) {
        if (subscription.cardId() == null && subscription.paymentMethodId() == null) return null;
        return BillingInfo.builder()
                .type(subscription.cardId() != null ? PaymentMethodType.CREDIT_CARD : PaymentMethodType.UNKNOWN)
                .card(Card.builder().brand(subscription.paymentMethodId()).build())
                .build();
    }
}
