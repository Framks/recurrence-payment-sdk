package com.recurrence.contract.domain.request;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.PlanInterval;
import com.recurrence.contract.domain.commons.PlanTrial;
import com.recurrence.contract.domain.enums.Currency;
import com.recurrence.contract.domain.enums.PaymentMethodType;
import com.recurrence.contract.domain.enums.PlanIntervalType;

import java.util.List;

public class CreatePlanRequest {
    private final String name;
    private final Amount amount;
    private final PlanInterval interval;
    private final Integer billingCycles;
    private final PlanTrial trial;
    private final List<PaymentMethodType> paymentMethods;

    private CreatePlanRequest(Builder builder) {
        this.name = builder.name;
        this.amount = builder.amount;
        this.interval = builder.interval;
        this.billingCycles = builder.billingCycles;
        this.trial = builder.trial;
        this.paymentMethods = builder.paymentMethods;
    }

    public String getName() {
        return name;
    }

    public Amount getAmount() {
        return amount;
    }

    public PlanInterval getInterval() {
        return interval;
    }

    public Integer getBillingCycles() {
        return billingCycles;
    }

    public PlanTrial getTrial() {
        return trial;
    }

    public List<PaymentMethodType> getPaymentMethods() {
        return paymentMethods;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private Amount amount;
        private PlanInterval interval;
        private Integer billingCycles;
        private PlanTrial trial;
        private List<PaymentMethodType> paymentMethods;

        private Builder() { }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder amount(Integer value, Currency currency) {
            this.amount = new Amount(value, currency);
            return this;
        }

        public Builder interval(PlanIntervalType type, Integer frequency) {
            this.interval = new PlanInterval(type, frequency);
            return this;
        }

        public Builder billingCycles(Integer billingCycles) {
            this.billingCycles = billingCycles;
            return this;
        }

        public Builder trial(Integer days) {
            this.trial = new PlanTrial(days);
            return this;
        }

        public Builder paymentMethods(List<PaymentMethodType> paymentMethods) {
            this.paymentMethods = paymentMethods;
            return this;
        }

        public CreatePlanRequest build() {
            return new CreatePlanRequest(this);
        }
    }
}
