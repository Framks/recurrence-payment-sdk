package com.recurrence.pagbank.domain.plan;

import com.recurrence.pagbank.domain.commons.Amount;

import java.time.OffsetDateTime;
import java.util.List;

final public class PagBankPlan {
    private String id;
    private String reference_id;
    private String status;
    private String name;
    private String description;
    private Amount amount;
    private int setup_fee;
    private int limit_subscriptions;
    private PagBankPlanInterval interval;
    private int billing_cycles;
    private PagBankPlanTrial trial;
    private List<String> payment_method;
    private OffsetDateTime created_at;
    private OffsetDateTime updated_at;
    private Boolean editable;

    private PagBankPlan(Builder builder) {
        this.reference_id = builder.referenceId;
        this.name = builder.name;
        this.description = builder.description;
        this.amount = builder.amount;
        this.setup_fee = builder.setupFee;
        this.limit_subscriptions = builder.limitSubscriptions;
        this.interval = builder.pagBankPlanInterval;
        this.billing_cycles = builder.billingCycles;
        this.trial = builder.pagBankPlanTrial;
        this.payment_method = builder.paymentMethod;
    }

    public PagBankPlan() {
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

    public String getReference_id() {
        return reference_id;
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

    public int getSetup_fee() {
        return setup_fee;
    }

    public int getLimit_subscriptions() {
        return limit_subscriptions;
    }

    public PagBankPlanInterval getInterval() {
        return interval;
    }

    public int getBilling_cycles() {
        return billing_cycles;
    }

    public PagBankPlanTrial getTrial() {
        return trial;
    }

    public List<String> getPayment_method() {
        return payment_method;
    }

    public OffsetDateTime getCreated_at() {
        return created_at;
    }

    public OffsetDateTime getUpdated_at() {
        return updated_at;
    }

    public Boolean getEditable() {
        return editable;
    }
}
