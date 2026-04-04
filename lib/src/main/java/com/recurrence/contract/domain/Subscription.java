package com.recurrence.contract.domain;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.BillingInfo;
import java.time.OffsetDateTime;
import java.util.List;

public class Subscription {
    private String id;
    private String referenceId;
    private String status;
    private Boolean proRata;
    private String nextInvoiceAt;
    private Coupon coupon;
    private Amount amount;
    private Plan plan;
    private List<BillingInfo> paymentMethod;
    private Customer customer;
    private String expAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private BestInvoiceDate bestInvoiceDate;
    private Boolean splitEnabled;
    private Splits splits;

    private Subscription(Builder builder) {
        this.id = builder.id;
        this.referenceId = builder.referenceId;
        this.status = builder.status;
        this.proRata = builder.proRata;
        this.nextInvoiceAt = builder.nextInvoiceAt;
        this.coupon = builder.coupon;
        this.amount = builder.amount;
        this.plan = builder.plan;
        this.paymentMethod = builder.paymentMethod;
        this.customer = builder.customer;
        this.expAt = builder.expAt;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.bestInvoiceDate = builder.bestInvoiceDate;
        this.splitEnabled = builder.splitEnabled;
        this.splits = builder.splits;
    }

    public record BestInvoiceDate(
        String day,
        String month
    ) { }
    public record Splits(
        String method,
        List<Receiver> receivers
    ) { }
    public record Receiver(
        String accountId,
        int amount
    ) { }
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String referenceId;
        private String status;
        private Boolean proRata;
        private String nextInvoiceAt;
        private Coupon coupon;
        private Amount amount;
        private Plan plan;
        private List<BillingInfo> paymentMethod;
        private Customer customer;
        private String expAt;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private BestInvoiceDate bestInvoiceDate;
        private Boolean splitEnabled;
        private Splits splits;

        private Builder() {}

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder referenceId(String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder proRata(Boolean proRata) {
            this.proRata = proRata;
            return this;
        }

        public Builder nextInvoiceAt(String nextInvoiceAt) {
            this.nextInvoiceAt = nextInvoiceAt;
            return this;
        }

        public Builder coupon(Coupon coupon) {
            this.coupon = coupon;
            return this;
        }

        public Builder amount(Amount amount) {
            this.amount = amount;
            return this;
        }

        public Builder plan(Plan plan) {
            this.plan = plan;
            return this;
        }

        public Builder paymentMethod(List<BillingInfo> paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder expAt(String expAt) {
            this.expAt = expAt;
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

        public Builder bestInvoiceDate(String day, String month) {
            this.bestInvoiceDate = new BestInvoiceDate(day, month);
            return this;
        }

        public Builder splitEnabled(Boolean splitEnabled) {
            this.splitEnabled = splitEnabled;
            return this;
        }

        public Builder splits(Splits splits) {
            this.splits = splits;
            return this;
        }

        public Subscription build() {
            return new Subscription(this);
        }
    }

    // getters
    public String getId() {
        return id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getStatus() {
        return status;
    }

    public Boolean getProRata() {
        return proRata;
    }

    public String getNextInvoiceAt() {
        return nextInvoiceAt;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public Amount getAmount() {
        return amount;
    }

    public Plan getPlan() {
        return plan;
    }

    public List<BillingInfo> getPaymentMethod() {
        return paymentMethod;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getExpAt() {
        return expAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getSplitEnabled() {
        return splitEnabled;
    }

    public Splits getSplits() {
        return splits;
    }

    public BestInvoiceDate getBestInvoiceDate() {
        return bestInvoiceDate;
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProRata(Boolean proRata) {
        this.proRata = proRata;
    }

    public void setNextInvoiceAt(String nextInvoiceAt) {
        this.nextInvoiceAt = nextInvoiceAt;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public void setPaymentMethod(List<BillingInfo> paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setExpAt(String expAt) {
        this.expAt = expAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSplitEnabled(Boolean splitEnabled) {
        this.splitEnabled = splitEnabled;
    }

    public void setSplits(Splits splits) {
        this.splits = splits;
    }

    public void setBestInvoiceDate(BestInvoiceDate bestInvoiceDate) {
        this.bestInvoiceDate = bestInvoiceDate;
    }
}