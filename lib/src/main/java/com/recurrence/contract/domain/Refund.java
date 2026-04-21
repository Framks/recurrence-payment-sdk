package com.recurrence.contract.domain;

import com.recurrence.contract.domain.commons.Amount;
import java.time.OffsetDateTime;

public class Refund {
    private String id;
    private String status;
    private String type;
    private OffsetDateTime createdAt;
    private Payment payment;
    private Amount amount;

    public Refund() {}

    private Refund(Builder builder) {
        this.id = builder.id;
        this.status = builder.status;
        this.type = builder.type;
        this.createdAt = builder.createdAt;
        this.payment = builder.paymentId;
        this.amount = builder.amount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() { return id; }
    public String getStatus() { return status; }
    public String getType() { return type; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public Payment getPayment() { return payment; }
    public Amount getAmount() { return amount; }

    public static class Builder {
        private String id;
        private String status;
        private String type;
        private OffsetDateTime createdAt;
        private Payment paymentId;
        private Amount amount;

        public Builder id(String id) { this.id = id; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder type(String type) { this.type = type; return this; }
        public Builder createdAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder paymentId(Payment paymentId) { this.paymentId = paymentId; return this; }
        public Builder amount(Amount amount) { this.amount = amount; return this; }

        public Refund build() { return new Refund(this); }
    }
}
