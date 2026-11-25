package com.recurrence.contract.domain;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.PlanInterval;
import com.recurrence.contract.domain.commons.PlanTrial;
import com.recurrence.contract.domain.enums.Currency;
import com.recurrence.contract.domain.enums.PaymentMethodType;
import com.recurrence.contract.domain.enums.PlanIntervalType;
import com.recurrence.contract.domain.enums.PlanStatus;
import java.time.OffsetDateTime;
import java.util.List;

final public class Plan {
    private final String id;
    private final PlanStatus status;
    private final String name;
    private final Amount amount;
    private final PlanInterval interval;
    private final Integer billingCycles;
    private final PlanTrial trial;
    private final List<PaymentMethodType> paymentMethods;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    private Plan(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.status = builder.status;
        this.amount = builder.amount;
        this.interval = builder.interval;
        this.billingCycles = builder.billingCycles;
        this.trial = builder.trial;
        this.paymentMethods = builder.paymentMethods;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public String getId() {
        return id;
    }

    public PlanStatus getStatus() {
        return status;
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String name;
        private PlanStatus status;
        private Amount amount;
        private PlanInterval interval;
        private Integer billingCycles;
        private PlanTrial trial;
        private List<PaymentMethodType> paymentMethods;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        private Builder() { }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder status(PlanStatus status) {
            this.status = status;
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

        public Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Plan build() {
            return new Plan(this);
        }
    }
}
