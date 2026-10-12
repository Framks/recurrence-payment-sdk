package com.recurrence.contract.domain;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.CustomerReference;
import com.recurrence.contract.domain.enums.PaymentStatus;

import java.time.OffsetDateTime;

public final class Payment {
    private final String id;
    private final PaymentStatus status;
    private final String invoiceId;
    private final Amount amount;
    private final BillingInfo paymentMethod;
    private final CustomerReference customer;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    private Payment(Builder builder) {
        this.id = builder.id;
        this.status = builder.status;
        this.invoiceId = builder.invoiceId;
        this.amount = builder.amount;
        this.paymentMethod = builder.paymentMethod;
        this.customer = builder.customer;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() { return id; }
    public PaymentStatus getStatus() { return status; }
    public String getInvoiceId() { return invoiceId; }
    public Amount getAmount() { return amount; }
    public BillingInfo getPaymentMethod() { return paymentMethod; }
    public CustomerReference getCustomer() { return customer; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public static final class Builder {
        private String id;
        private PaymentStatus status;
        private String invoiceId;
        private Amount amount;
        private BillingInfo paymentMethod;
        private CustomerReference customer;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        private Builder() {}

        public Builder id(String id) { this.id = id; return this; }
        public Builder status(PaymentStatus status) { this.status = status; return this; }
        public Builder invoiceId(String invoiceId) { this.invoiceId = invoiceId; return this; }
        public Builder amount(Amount amount) { this.amount = amount; return this; }
        public Builder paymentMethod(BillingInfo paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder customer(CustomerReference customer) { this.customer = customer; return this; }
        public Builder createdAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Payment build() { return new Payment(this); }
    }
}
