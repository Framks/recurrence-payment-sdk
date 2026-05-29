package com.recurrence.pagbank.domain.coupon;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class PagBankCoupon {
    private String id;
    private String referenceId;
    private String name;                    // ToDo deve ser obrigatorio esse campo.
    private String description;
    private PagBankCouponDiscount discount; // ToDo deve ser obrigatorio esse campo.
    private String status;
    private PagBankCouponDuration duration; // ToDo deve ser obrigatorio esse campo.
    private Integer redemptionLimit;
    @JsonProperty("exp_at")
    private String expiresAt;
    private Boolean inUse;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private PagBankCoupon(Builder builder){
        this.id = builder.id;
        this.referenceId = builder.referenceId;
        this.name = builder.name;
        this.description = builder.description;
        this.discount = builder.discount;
        this.status = builder.status;
        this.duration = builder.duration;
        this.redemptionLimit = builder.redemptionLimit;
        this.expiresAt = builder.expiresAt;
        this.inUse = builder.inUse;
        this.createdAt = builder.createAt;
        this.updatedAt = builder.updatedAt;
    }

    public PagBankCoupon() {
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PagBankCouponDiscount getDiscount() {
        return discount;
    }

    public void setDiscount(PagBankCouponDiscount discount) {
        this.discount = discount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public PagBankCouponDuration getDuration() {
        return duration;
    }

    public void setDuration(PagBankCouponDuration duration) {
        this.duration = duration;
    }

    public Integer getRedemptionLimit() {
        return redemptionLimit;
    }

    public void setRedemptionLimit(Integer redemptionLimit) {
        this.redemptionLimit = redemptionLimit;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public Boolean getInUse() {
        return inUse;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder{
        private String id;
        private String name;
        private String referenceId;
        private String description;
        private String status;
        private PagBankCouponDiscount discount;
        private PagBankCouponDuration duration;
        private Integer redemptionLimit;
        private String expiresAt;
        private Boolean inUse;
        private OffsetDateTime createAt;
        private OffsetDateTime updatedAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder inUse(Boolean inUse) {
            this.inUse = inUse;
            return this;
        }

        public Builder createAt(OffsetDateTime createAt) {
            this.createAt = createAt;
            return this;
        }

        public Builder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

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

        public Builder discount(PagBankCouponDiscount couponDiscount) {
            this.discount = couponDiscount;
            return this;
        }

        public Builder duration(PagBankCouponDuration couponDuration) {
            this.duration = couponDuration;
            return this;
        }

        public Builder redemptionLimit(Integer redemptionLimit) {
            this.redemptionLimit = redemptionLimit;
            return this;
        }

        public Builder expiresAt(String expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public PagBankCoupon build() {
            return new PagBankCoupon(this);
        }
    }
}
