package org.example.pagbank.request.payload;

import java.time.OffsetDateTime;
import java.util.List;

public class Plan {
    private String id;
    private String referenceId;
    private String status;
    private String name;
    private String description;
    private Amount amount;
    private int setupFee;
    private int limitSubscriptions;
    private Interval interval;
    private int billingCycles;
    private Trial trial;
    private List<String> paymentMethod;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean editable;

    // construtor default (Jackson)
    public Plan() {
    }

    // construtor usado pelo Builder
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
        this.paymentMethod = builder.paymentMethod;
    }

    // ===== Builder =====
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
        private Interval interval;
        private int billingCycles;
        private Trial trial;
        private List<String> paymentMethod;

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

        public Builder amount(Amount amount) {
            this.amount = amount;
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

        public Builder interval(Interval interval) {
            this.interval = interval;
            return this;
        }

        public Builder billingCycles(int billingCycles) {
            this.billingCycles = billingCycles;
            return this;
        }

        public Builder trial(Trial trial) {
            this.trial = trial;
            return this;
        }

        public Builder paymentMethod(List<String> paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Plan build() {
            validate();
            return new Plan(this);
        }

        private void validate() {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Plan name is required");
            }
            if (amount == null) {
                throw new IllegalArgumentException("Amount is required");
            }
            if (interval == null) {
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

    public Interval getInterval() {
        return interval;
    }

    public int getBillingCycles() {
        return billingCycles;
    }

    public Trial getTrial() {
        return trial;
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
