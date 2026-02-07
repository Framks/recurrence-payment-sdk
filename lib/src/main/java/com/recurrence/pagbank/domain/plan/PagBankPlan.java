package com.recurrence.pagbank.domain.plan;

import com.recurrence.pagbank.domain.commons.Amount;

import java.time.OffsetDateTime;
import java.util.List;

final public class PagBankPlan {
    private String id;
    private final String referenceId;
    private String status;
    private final String name;
    private final String description;
    private final Amount amount;
    private final int setupFee;
    private final int limitSubscriptions;
    private final PagBankPlanInterval pagBankPlanInterval;
    private final int billingCycles;
    private final PagBankPlanTrial pagBankPlanTrial;
    private final List<String> paymentMethod;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean editable;

    private PagBankPlan(Builder builder) {
        this.referenceId = builder.referenceId;
        this.name = builder.name;
        this.description = builder.description;
        this.amount = builder.amount;
        this.setupFee = builder.setupFee;
        this.limitSubscriptions = builder.limitSubscriptions;
        this.pagBankPlanInterval = builder.pagBankPlanInterval;
        this.billingCycles = builder.billingCycles;
        this.pagBankPlanTrial = builder.pagBankPlanTrial;
        this.paymentMethod = builder.paymentMethod;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String referenceId;
        private String description;
        private Amount amount;
        private int setupFee;
        private int limitSubscriptions;
        private PagBankPlanInterval pagBankPlanInterval;
        private int billingCycles;
        private PagBankPlanTrial pagBankPlanTrial;
        private List<String> paymentMethod;

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder referenceId(final String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public Builder description(final String description) {
            this.description = description;
            return this;
        }

        public Builder amount(final Amount amount) {
            this.amount = amount;
            return this;
        }

        public Builder setupFee(final int setupFee) {
            this.setupFee = setupFee;
            return this;
        }

        public Builder limitSubscriptions(final int limitSubscriptions) {
            this.limitSubscriptions = limitSubscriptions;
            return this;
        }

        public Builder interval(final PagBankPlanInterval pagBankPlanInterval) {
            this.pagBankPlanInterval = pagBankPlanInterval;
            return this;
        }

        public Builder billingCycles(final int billingCycles) {
            this.billingCycles = billingCycles;
            return this;
        }

        public Builder trial(final PagBankPlanTrial pagBankPlanTrial) {
            this.pagBankPlanTrial = pagBankPlanTrial;
            return this;
        }

        public Builder paymentMethod(final List<String> paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public PagBankPlan build() {
            validate();
            return new PagBankPlan(this);
        }

        private void validate() {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Plan name is required");
            }
            if (amount == null) {
                throw new IllegalArgumentException("Amount is required");
            }
            if (pagBankPlanInterval == null) {
                throw new IllegalArgumentException("Interval is required");
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Amount getAmount() {
        return amount;
    }

    public int getSetupFee() {
        return setupFee;
    }

    public int getLimitSubscriptions() {
        return limitSubscriptions;
    }

    public PagBankPlanInterval getInterval() {
        return pagBankPlanInterval;
    }

    public int getBillingCycles() {
        return billingCycles;
    }

    public PagBankPlanTrial getTrial() {
        return pagBankPlanTrial;
    }

    public List<String> getPaymentMethod() {
        return paymentMethod;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getEditable() {
        return editable;
    }
}
