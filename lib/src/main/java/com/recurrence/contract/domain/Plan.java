package com.recurrence.contract.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

final public class Plan {
    private String id;
    private final String referenceId;
    private String status;
    private final String name;
    private final String description;
    private final PlanAmount amount;
    private final int setupFee;
    private final int limitSubscriptions;
    private final PlanInterval interval;
    private final int billingCycles;
    private final PlanTrial trial;
    private final List<String> paymentMethods;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean editable;

    public record PlanInterval(
            int length,
            String unit
    ) {
    }

    public record PlanAmount(
            int value,
            String currency
    ) {

    }

    public record PlanTrial(
            int days,
            Boolean enabled,
            Boolean hold_setup_fee
    ) {
    }

    private Plan(Builder builder) {
        this.referenceId = builder.referenceId;
        this.name = builder.name;
        this.description = builder.description;
        this.amount = builder.amount;
        this.setupFee = builder.setupFee;
        this.limitSubscriptions = builder.limitSubscriptions;
        this.interval = builder.interval;
        this.billingCycles = builder.billingCycles;
        this.trial = builder.trial;
        this.paymentMethods = builder.paymentMethods;
    }

    public void setId(String id) {
        this.id = id;
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

    public PlanAmount getAmount() {
        return amount;
    }

    public int getSetupFee() {
        return setupFee;
    }

    public int getLimitSubscriptions() {
        return limitSubscriptions;
    }

    public PlanInterval getInterval() {
        return interval;
    }

    public int getBillingCycles() {
        return billingCycles;
    }

    public PlanTrial getTrial() {
        return trial;
    }

    public List<String> getPaymentMethods() {
        return paymentMethods;
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String name;
        private String referenceId;
        private PlanAmount amount;
        private PlanInterval interval;

        private String description;
        private int setupFee;
        private int limitSubscriptions;
        private int billingCycles;
        private PlanTrial trial;
        private List<String> paymentMethods;

        private Builder() {}

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder referenceId(String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder amount(int value, String currency) {
            this.amount = new PlanAmount(value, currency);
            return this;
        }

        public Builder interval(int length, String unit) {
            this.interval = new PlanInterval(length, unit);
            return this;
        }

        public Builder setupFee(int setupFee) {
            this.setupFee = setupFee;
            return this;
        }

        public Builder limitSubscriptions(int limitSubscriptions) {
            this.limitSubscriptions = limitSubscriptions;
            return this;
        }

        public Builder billingCycles(int billingCycles) {
            this.billingCycles = billingCycles;
            return this;
        }

        public Builder trial(int days, Boolean enabled, Boolean holdSetupFee) {
            this.trial = new PlanTrial(days, enabled, holdSetupFee);
            return this;
        }

        public Builder paymentMethods(List<String> paymentMethods) {
            this.paymentMethods = paymentMethods;
            return this;
        }

        public Plan build() {
            validate();
            return new Plan(this);
        }

        private void validate() {
            if (name.isBlank()) {
                throw new IllegalArgumentException("Plan name must not be blank");
            }
            if (amount == null) {
                throw new IllegalArgumentException("Plan amount is required");
            }
            if (interval == null) {
                throw new IllegalArgumentException("Plan interval is required");
            }
        }
    }
}
