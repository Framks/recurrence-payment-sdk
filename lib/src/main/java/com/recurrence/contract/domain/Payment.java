package com.recurrence.contract.domain;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.BillingInfo;
import java.time.OffsetDateTime;

public class Payment {
    private String id;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String invoiceId;
    private Amount amount;
    private BillingInfo paymentMethod;
    private Customer customer;

    public Payment() {}

    private Payment(Builder builder) {
        this.id = builder.id;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.invoiceId = builder.invoiceId;
        this.amount = builder.amount;
        this.customer = builder.customer;
        this.paymentMethod = builder.paymentMethod;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() { return id; }
    public String getStatus() { return status; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public String getInvoiceId() { return invoiceId; }
    public Amount getAmount() { return amount; }
    public Customer getCustomer() { return customer; }

    public static class Builder {
        private String id;
        private String status;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private String invoiceId;
        private Amount amount;
        private Customer customer;
        private BillingInfo paymentMethod;

        public Builder id(String id) { this.id = id; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder createdAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder invoiceId(String invoiceId) { this.invoiceId = invoiceId; return this; }
        public Builder amount(Amount amount) { this.amount = amount; return this; }
        public Builder customer(Customer customer) { this.customer = customer; return this; }
        public Builder paymentMethod(BillingInfo paymentMethod) { this.paymentMethod = paymentMethod; return this; }

        public Payment build() { return new Payment(this); }
    }
}
