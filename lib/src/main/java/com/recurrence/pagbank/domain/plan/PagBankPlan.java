package com.recurrence.pagbank.domain.plan;

import com.recurrence.pagbank.domain.commons.PagBankAmount;
import java.time.OffsetDateTime;
import java.util.List;

final public class PagBankPlan {
    private String id;
    private String referenceId;
    private String status;
    private String name;
    private String description;
    private PagBankAmount pagBankAmount;
    private int setupFee;
    private int limitSubscriptions;
    private PagBankPlanInterval interval;
    private int billingCycles;
    private PagBankPlanTrial trial;
    private List<String> paymentMethod;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean editable;

    private PagBankPlan(Builder builder) {
        this.id = builder.id;
        this.referenceId = builder.referenceId;
        this.name = builder.name;
        this.status = builder.status;
        this.description = builder.description;
        this.pagBankAmount = builder.pagBankAmount;
        this.setupFee = builder.setupFee;
        this.limitSubscriptions = builder.limitSubscriptions;
        this.interval = builder.pagBankPlanInterval;
        this.billingCycles = builder.billingCycles;
        this.trial = builder.pagBankPlanTrial;
        this.paymentMethod = builder.paymentMethod;
        this.createdAt = builder.created_at;
        this.updatedAt = builder.updated_at;
        this.editable = builder.editable;
    }

    public PagBankPlan() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String status;
        private String name;
        private String referenceId;
        private String description;
        private PagBankAmount pagBankAmount;
        private int setupFee;
        private int limitSubscriptions;
        private PagBankPlanInterval pagBankPlanInterval;
        private int billingCycles;
        private PagBankPlanTrial pagBankPlanTrial;
        private List<String> paymentMethod;
        private OffsetDateTime created_at;
        private OffsetDateTime updated_at;
        private Boolean editable;

        public Builder id(final String id) {
            this.id = id;
            return this;
        }

        public Builder status(final String status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(final OffsetDateTime created_at) {
            this.created_at = created_at;
            return this;
        }

        public Builder updatedAt(final OffsetDateTime updated_at) {
            this.updated_at = updated_at;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder editable(final Boolean editable) {
            this.editable = editable;
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

        public Builder amount(final PagBankAmount pagBankAmount) {
            this.pagBankAmount = pagBankAmount;
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
            return new PagBankPlan(this);
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

    public PagBankAmount getAmount() {
        return pagBankAmount;
    }

    public int getSetupFee() {
        return setupFee;
    }

    public int getLimitSubscriptions() {
        return limitSubscriptions;
    }

    public PagBankPlanInterval getInterval() {
        return interval;
    }

    public int getBillingCycles() {
        return billingCycles;
    }

    public PagBankPlanTrial getTrial() {
        return trial;
    }

    public List<String> getPaymentMethod() {
        return paymentMethod;
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
