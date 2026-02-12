package com.recurrence.pagbank.domain.coupon;

import java.time.OffsetDateTime;

public class PagBankCoupon {
    private String id;
    private String referenceId;
    private String name;
    private String description;
    private PagBankCouponDiscount discount;
    private String status;
    private PagBankCouponDuration duration;
    private int redemptionLimit;
    private OffsetDateTime expiresAt;
    private Boolean inUse;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private PagBankCoupon(Builder builder){
        this.referenceId = builder.referenceId;
        this.name = builder.name;
        this.description = builder.description;
        this.discount = builder.discount;
        this.duration = builder.duration;
        this.redemptionLimit = builder.redemptionLimit;
        this.expiresAt = builder.expiresAt;
        this.status = builder.status;
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

    public int getRedemptionLimit() {
        return redemptionLimit;
    }

    public void setRedemptionLimit(int redemptionLimit) {
        this.redemptionLimit = redemptionLimit;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
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
        private String name;
        private String referenceId;
        private String description;
        private String status;
        private PagBankCouponDiscount discount;
        private PagBankCouponDuration duration;
        private int redemptionLimit;
        private OffsetDateTime expiresAt;

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

        public Builder redemptionLimit(int redemptionLimit) {
            this.redemptionLimit = redemptionLimit;
            return this;
        }

        public Builder expiresAt(OffsetDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public PagBankCoupon build() {
            validate();
            return new PagBankCoupon(this);
        }

        private void validate() {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name is required");
            }
            if (discount == null) {
                throw new IllegalArgumentException("Discount is required");
            }
            if (duration == null) {
                throw new IllegalArgumentException("Duration is required");
            }
            if (redemptionLimit <= 0) {
                throw new IllegalArgumentException("Redemption limit is required");
            }
            if (expiresAt == null || expiresAt.isBefore(OffsetDateTime.now())) {
                throw new IllegalArgumentException("Expiration date is required and must be in the future");
            }
        }
    }
}
