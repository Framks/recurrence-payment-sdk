package com.recurrence.pagbank.domain.subscription;

import com.recurrence.pagbank.domain.commons.PagBankAmount;
import com.recurrence.pagbank.domain.commons.PagBankBillingInfo;
import com.recurrence.pagbank.domain.coupon.PagBankCoupon;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import com.recurrence.pagbank.domain.plan.PagBankPlan;
import java.time.OffsetDateTime;
import java.util.List;

public class PagBankSubscription {
    private String id;
    private String referenceId;
    private String status;
    private Boolean proRata;
    private String nextInvoiceAt;
    private PagBankCoupon coupon;
    private PagBankAmount amount;
    private PagBankPlan plan;
    private List<PagBankBillingInfo> paymentMethod;
    private PagBankCustomer customer;
    private String expAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private BestInvoiceDate bestInvoiceDate;
    private Boolean splitEnabled;
    private Splits splits;

    public PagBankSubscription(){}

    public PagBankSubscription(
        String id,
        String referenceId,
        String status,
        Boolean proRata,
        String nextInvoiceAt,
        PagBankCoupon coupon,
        PagBankAmount amount,
        PagBankPlan plan,
        List<PagBankBillingInfo> paymentMethod,
        PagBankCustomer customer,
        String expAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        BestInvoiceDate bestInvoiceDate,
        Boolean splitEnabled,
        Splits splits
    ){
        this.id = id;
        this.referenceId = referenceId;
        this.status = status;
        this.proRata = proRata;
        this.nextInvoiceAt = nextInvoiceAt;
        this.coupon = coupon;
        this.amount = amount;
        this.plan = plan;
        this.paymentMethod = paymentMethod;
        this.customer = customer;
        this.expAt = expAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.splitEnabled = splitEnabled;
        this.splits = splits;
        this.bestInvoiceDate = bestInvoiceDate;
    }

    private PagBankSubscription(Builder builder) {
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
        this.splitEnabled = builder.splitEnabled;
        this.splits = builder.splits;
        this.bestInvoiceDate = builder.bestInvoiceDate;
    }

    public record Splits(
        String method,
        List<Receiver> receivers
    ){}
    public record Receiver(
        Account account,
        PagBankAmount amount
    ){}
    public record Account(
        String id
    ){}
    public record BestInvoiceDate(
        String day,
        String month
    ){}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private String id;
        private String referenceId;
        private String status;
        private Boolean proRata;
        private String nextInvoiceAt;
        private PagBankCoupon coupon;
        private PagBankAmount amount;
        private PagBankPlan plan;
        private List<PagBankBillingInfo> paymentMethod;
        private PagBankCustomer customer;
        private String expAt;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private BestInvoiceDate bestInvoiceDate;
        private Boolean splitEnabled;
        private Splits splits;

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

        public Builder coupon(PagBankCoupon coupon) {
            this.coupon = coupon;
            return this;
        }

        public Builder amount(PagBankAmount amount) {
            this.amount = amount;
            return this;
        }

        public Builder plan(PagBankPlan plan) {
            this.plan = plan;
            return this;
        }

        public Builder paymentMethod(List<PagBankBillingInfo> paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder customer(PagBankCustomer customer) {
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

        public Builder bestInvoiceDate(BestInvoiceDate bestInvoiceDate) {
            this.bestInvoiceDate = bestInvoiceDate;
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

        public PagBankSubscription build() {
            return new PagBankSubscription(this);
        }
    }

    // getters
    public String getId() {return id;}

    public String getReferenceId() {return referenceId;}

    public String getStatus() {return status;}

    public Boolean getProRata() {return proRata;}

    public String getNextInvoiceAt() {return nextInvoiceAt;}

    public PagBankCoupon getCoupon() {return coupon;}

    public PagBankAmount getAmount() {return amount;}

    public PagBankPlan getPlan() {return plan;}

    public List<PagBankBillingInfo> getPaymentMethod() {return paymentMethod;}

    public PagBankCustomer getCustomer() {return customer;}

    public String getExpAt() {return expAt;}

    public OffsetDateTime getCreatedAt() {return createdAt;}

    public OffsetDateTime getUpdatedAt() {return updatedAt;}

    public Boolean getSplitEnabled() {return splitEnabled;}

    public Splits getSplits() {return splits;}

    public BestInvoiceDate getBestInvoiceDate() {return bestInvoiceDate;}

    // setters
    public void setId(String id) {this.id = id;}

    public void setReferenceId(String referenceId) {this.referenceId = referenceId;}

    public void setStatus(String status) {this.status = status;}

    public void setProRata(Boolean proRata) {this.proRata = proRata;}

    public void setNextInvoiceAt(String nextInvoiceAt) {this.nextInvoiceAt = nextInvoiceAt;}

    public void setCoupon(PagBankCoupon coupon) {this.coupon = coupon;}

    public void setAmount(PagBankAmount amount) {this.amount = amount;}

    public void setPlan(PagBankPlan plan) {this.plan = plan;}

    public void setPaymentMethod(List<PagBankBillingInfo> paymentMethod) {this.paymentMethod = paymentMethod;}

    public void setCustomer(PagBankCustomer customer) {this.customer = customer;}

    public void setExpAt(String expAt) {this.expAt = expAt;}

    public void setCreatedAt(OffsetDateTime createdAt) {this.createdAt = createdAt;}

    public void setUpdatedAt(OffsetDateTime updatedAt) {this.updatedAt = updatedAt;}

    public void setSplitEnabled(Boolean splitEnabled) {this.splitEnabled = splitEnabled;}

    public void setSplits(Splits splits) {this.splits = splits;}

    public void setBestInvoiceDate(BestInvoiceDate bestInvoiceDate) {this.bestInvoiceDate = bestInvoiceDate;}
}
