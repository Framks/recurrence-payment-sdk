package com.recurrence.contract.domain.request;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.enums.Currency;

import java.util.List;

/**
 * Dados para criar uma assinatura vinculada a um plano.
 *
 * <pre>{@code
 * CreateSubscriptionRequest request = CreateSubscriptionRequest.builder()
 *     .planId("PLAN_123")
 *     .referenceId("PEDIDO-2026-001")
 *     .customer(customer)
 *     .paymentMethod(List.of(billingInfo))
 *     .build();
 * }</pre>
 */
public final class CreateSubscriptionRequest {

    private final String planId;
    private final String referenceId;
    private final Amount amount;
    private final List<BillingInfo> paymentMethod;
    private final CustomerRequest customer;

    private CreateSubscriptionRequest(Builder builder) {
        this.planId = builder.planId;
        this.referenceId = builder.referenceId;
        this.amount = builder.amount;
        this.paymentMethod = builder.paymentMethod;
        this.customer = builder.customer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPlanId() {
        return planId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public Amount getAmount() {
        return amount;
    }

    public List<BillingInfo> getPaymentMethod() {
        return paymentMethod;
    }

    public CustomerRequest getCustomer() {
        return customer;
    }

    public static final class Builder {
        private String planId;
        private String referenceId;
        private Amount amount;
        private List<BillingInfo> paymentMethod;
        private CustomerRequest customer;

        private Builder() { }

        public Builder planId(String planId) {
            this.planId = planId;
            return this;
        }

        public Builder referenceId(String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public Builder amount(Integer value, Currency currency) {
            this.amount = new Amount(value, currency);
            return this;
        }

        public Builder paymentMethod(List<BillingInfo> paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder customer(CustomerRequest customer) {
            this.customer = customer;
            return this;
        }

        public CreateSubscriptionRequest build() {
            return new CreateSubscriptionRequest(this);
        }
    }
}
