package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.Phone;
import com.recurrence.contract.domain.request.CreateSubscriptionRequest;
import com.recurrence.contract.domain.request.CustomerRequest;
import com.recurrence.contract.domain.request.UpdateSubscriptionRequest;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.pagbank.domain.commons.PagBankBillingInfo;
import com.recurrence.pagbank.domain.commons.PagBankIdRef;
import com.recurrence.pagbank.domain.commons.PagBankPhone;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import com.recurrence.pagbank.domain.subscription.PagBankSubscription;
import com.recurrence.pagbank.domain.subscription.PagBankSubscriptionRequest;
import com.recurrence.pagbank.domain.subscription.PagBankSubscriptionUpdateRequest;
import com.recurrence.pagbank.domain.subscription.PagbankListSubscription;

import java.util.List;

public final class PagBankSubscriptionMapper {

    private PagBankSubscriptionMapper() {}

    /**
     * O PagBank recebe o cartão criptografado dentro do assinante
     * ({@code customer.billing_info}) e só o CVV em {@code payment_method}.
     */
    public static PagBankSubscriptionRequest toRequest(CreateSubscriptionRequest subscription) {
        BillingInfo paymentMethod = subscription.getPaymentMethod();
        return new PagBankSubscriptionRequest(
                subscription.getReferenceId(),
                new PagBankIdRef(subscription.getPlanId()),
                CommonsMapper.toPagBank(subscription.getAmount()),
                toPagBank(subscription.getCustomer(), paymentMethod),
                List.of(CommonsMapper.toPagBankPaymentMethod(paymentMethod))
        );
    }

    public static PagBankSubscriptionUpdateRequest toRequest(UpdateSubscriptionRequest subscription) {
        return new PagBankSubscriptionUpdateRequest(
                subscription.getPlanId() == null ? null : new PagBankIdRef(subscription.getPlanId()),
                CommonsMapper.toPagBank(subscription.getAmount())
        );
    }

    /** Corpo de {@code PUT /customers/{id}/billing_info}. */
    public static List<PagBankBillingInfo> toBillingInfoRequest(BillingInfo paymentMethod) {
        return List.of(CommonsMapper.toPagBankBillingInfo(paymentMethod));
    }

    public static Subscription toContract(PagBankSubscription subscription) {
        return Subscription.builder()
                .id(subscription.id())
                .referenceId(subscription.referenceId())
                .status(CommonsMapper.toSubscriptionStatus(subscription.status()))
                .amount(CommonsMapper.toContract(subscription.amount()))
                .plan(CommonsMapper.toPlanReference(subscription.plan()))
                .customer(CommonsMapper.toCustomerReference(subscription.customer()))
                .paymentMethod(CommonsMapper.firstBillingInfo(subscription.paymentMethod()))
                .nextInvoiceAt(CommonsMapper.toLocalDate(subscription.nextInvoiceAt()))
                .expAt(CommonsMapper.toLocalDate(subscription.expAt()))
                .createdAt(subscription.createdAt())
                .updatedAt(subscription.updatedAt())
                .build();
    }

    public static PageResponse<Subscription> toPage(PagbankListSubscription list) {
        return CommonsMapper.toPage(list.resultSet(), list.subscriptions(), PagBankSubscriptionMapper::toContract);
    }

    private static PagBankCustomer toPagBank(CustomerRequest customer, BillingInfo paymentMethod) {
        return PagBankCustomer.newCustomer(
                customer.getName(),
                customer.getEmail(),
                customer.getTaxId(),
                customer.getPhone() == null ? null : List.of(toPagBank(customer.getPhone())),
                List.of(CommonsMapper.toPagBankBillingInfo(paymentMethod))
        );
    }

    private static PagBankPhone toPagBank(Phone phone) {
        return new PagBankPhone(phone.getCountry(), phone.getArea(), phone.getNumber());
    }
}
