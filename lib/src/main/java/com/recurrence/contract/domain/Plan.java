package com.recurrence.contract.domain;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.PaymentMethodType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

final public class Plan {
    private String id;
    private final String referenceId;
    private String status;
    private final String name;
    private final String description;
    private final Amount amount;
    private final Integer setupFee;
    private final Integer limitSubscriptions;
    private final PlanInterval interval;
    private final Integer billingCycles;
    private final PlanTrial trial;
    private final List<PaymentMethodType> paymentMethods;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean editable;

    public record PlanInterval(
            Integer length,
            String unit
    ) { }

    public record PlanTrial(
            Integer days,
            Boolean enabled,
            Boolean hold_setup_fee
    ) { }

    private Plan(Builder builder) {
        this.id = builder.id;
        this.referenceId = builder.referenceId;
        this.name = builder.name;
        this.status = builder.status;
        this.description = builder.description;
        this.amount = builder.amount;
        this.setupFee = builder.setupFee;
        this.limitSubscriptions = builder.limitSubscriptions;
        this.interval = builder.interval;
        this.billingCycles = builder.billingCycles;
        this.trial = builder.trial;
        this.paymentMethods = builder.paymentMethods;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.editable = builder.editable;
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

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getSetupFee() {
        return setupFee;
    }

    public Integer getLimitSubscriptions() {
        return limitSubscriptions;
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

    public Boolean getEditable() {
        return editable;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String name;
        private String status;
        private String referenceId;
        private Amount amount;
        private PlanInterval interval;
        private String description;
        private Integer setupFee;
        private Integer limitSubscriptions;
        private Integer billingCycles;
        private PlanTrial trial;
        private List<PaymentMethodType> paymentMethods;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private Boolean editable;

        private Builder() { }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
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

        public Builder amount(Integer value, String currency) {
            this.amount = new Amount(value, currency);
            return this;
        }

        public Builder interval(Integer length, String unit) {
            this.interval = new PlanInterval(length, unit);
            return this;
        }

        public Builder setupFee(Integer setupFee) {
            this.setupFee = setupFee;
            return this;
        }

        public Builder limitSubscriptions(Integer limitSubscriptions) {
            this.limitSubscriptions = limitSubscriptions;
            return this;
        }

        public Builder billingCycles(Integer billingCycles) {
            this.billingCycles = billingCycles;
            return this;
        }

        public Builder trial(Integer days, Boolean enabled, Boolean holdSetupFee) {
            this.trial = new PlanTrial(days, enabled, holdSetupFee);
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

        public Builder editable(Boolean editable) {
            this.editable = editable;
            return this;
        }

        public Plan build() {
            //validate();
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
