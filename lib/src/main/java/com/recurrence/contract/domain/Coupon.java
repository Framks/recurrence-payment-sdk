package com.recurrence.contract.domain;

import java.time.OffsetDateTime;

final public class Coupon {
    private String id;
    private final String referenceId;
    private final String name;
    private final String description;
    private final CouponDiscount discount;
    private String status;
    private final CouponDuration duration;
    private final int redemptionLimit;
    private final OffsetDateTime expiresAt;
    private Boolean inUse;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public record CouponDiscount(
        int value,
        String type
    ) { }

    public record CouponDuration(
        String type,
        int occurrences
    ) { }

    private Coupon(Builder builder){
        this.referenceId = builder.referenceId;
        this.name = builder.name;
        this.description = builder.description;
        this.discount = builder.discount;
        this.duration = builder.duration;
        this.redemptionLimit = builder.redemptionLimit;
        this.expiresAt = builder.expiresAt;
        this.status = builder.status;
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

    public CouponDiscount getDiscount() {
        return discount;
    }

    public String getStatus() {
    return status;
    }

    public CouponDuration getDuration() {
        return duration;
    }

    public int getRedemptionLimit() {
        return redemptionLimit;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
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

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static Coupon.Builder builder() {
        return new Coupon.Builder();
    }

    public static final class Builder{
        private String name;
        private String referenceId;
        private String description;
        private String status;
        private CouponDiscount discount;
        private CouponDuration duration;
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

        public Builder discount(String type, int value) {
            this.discount = new CouponDiscount(value, type);
            return this;
        }

        public Builder duration(String type, int occurrences) {
            this.duration = new CouponDuration(type, occurrences);
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

        public Coupon build() {
            validate();
            return new Coupon(this);
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
