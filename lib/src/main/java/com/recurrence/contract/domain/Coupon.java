package com.recurrence.contract.domain;

import java.time.OffsetDateTime;

final public class Coupon {
    private final String id;
    private final String referenceId;
    private final String name;
    private final String description;
    private final CouponDiscount discount;
    private String status;                  // ToDo Devemos deixar o contrato com um tipo especifico?
    private final CouponDuration duration;
    private final Integer redemptionLimit;
    private final String expiresAt;
    private Boolean inUse;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public record CouponDiscount( // ToDo devemos deixar um type especifco exemplo o do paymentMethod
        Integer value,
        String type
    ) { }

    public record CouponDuration(  // ToDo devemos deixar um type especifco exemplo o do paymentMethod
        String type,
        Integer occurrences
    ) { }

    private Coupon(Builder builder){
        this.id = builder.id;
        this.referenceId = builder.referenceId;
        this.name = builder.name;
        this.description = builder.description;
        this.status = builder.status;
        this.inUse = builder.inUse;
        this.discount = builder.discount;
        this.duration = builder.duration;
        this.redemptionLimit = builder.redemptionLimit;
        this.expiresAt = builder.expiresAt;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;

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

    public Integer getRedemptionLimit() {
        return redemptionLimit;
    }

    public String getExpiresAt() {
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
        private String id;
        private String name;
        private String referenceId;
        private String description;
        private String status;
        private CouponDiscount discount;
        private CouponDuration duration;
        private Integer redemptionLimit;
        private String expiresAt;
        private Boolean inUse;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        public Builder createAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder inUse(Boolean inUse) {
            this.inUse = inUse;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
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

        public Builder discount(String type, Integer value) {
            this.discount = new CouponDiscount(value, type);
            return this;
        }

        public Builder duration(String type, Integer occurrences) {
            this.duration = new CouponDuration(type, occurrences);
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

        public Coupon build() {
            return new Coupon(this);
        }
    }
}
