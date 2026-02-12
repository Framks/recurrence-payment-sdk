package com.recurrence.pagbank.domain.plan;

import com.recurrence.pagbank.domain.commons.Amount;

import java.time.OffsetDateTime;
import java.util.List;

final public class PagBankPlan {
    private String id;
    private String referenceId;
    private String status;
    private String name;
    private String description;
    private Amount amount;
    private int setupFee;
    private int limitSubscriptions;
    private PagBankPlanInterval pagBankPlanInterval;
    private int billingCycles;
    private PagBankPlanTrial pagBankPlanTrial;
    private List<String> paymentMethod;
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

    public PagBankPlan(){

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

        public Builder interval(PagBankPlanInterval pagBankPlanInterval) {
            this.pagBankPlanInterval = pagBankPlanInterval;
            return this;
        }

        public Builder billingCycles(int billingCycles) {
            this.billingCycles = billingCycles;
            return this;
        }

        public Builder trial(PagBankPlanTrial pagBankPlanTrial) {
            this.pagBankPlanTrial = pagBankPlanTrial;
            return this;
        }

        public Builder paymentMethod(List<String> paymentMethod) {
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

    public void setId(String id) {
        this.id = id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public int getSetupFee() {
        return setupFee;
    }

    public void setSetupFee(int setupFee) {
        this.setupFee = setupFee;
    }

    public int getLimitSubscriptions() {
        return limitSubscriptions;
    }

    public void setLimitSubscriptions(int limitSubscriptions) {
        this.limitSubscriptions = limitSubscriptions;
    }

    public PagBankPlanInterval getInterval() {
        return pagBankPlanInterval;
    }

    public void setInterval(PagBankPlanInterval pagBankPlanInterval) {
        this.pagBankPlanInterval = pagBankPlanInterval;
    }

    public int getBillingCycles() {
        return billingCycles;
    }

    public void setBillingCycles(int billingCycles) {
        this.billingCycles = billingCycles;
    }

    public PagBankPlanTrial getTrial() {
        return pagBankPlanTrial;
    }

    public void setTrial(PagBankPlanTrial pagBankPlanTrial) {
        this.pagBankPlanTrial = pagBankPlanTrial;
    }

    public List<String> getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(List<String> paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }
}
