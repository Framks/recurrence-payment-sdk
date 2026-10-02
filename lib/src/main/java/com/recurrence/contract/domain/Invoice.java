package com.recurrence.contract.domain;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.CustomerReference;
import com.recurrence.contract.domain.commons.PlanReference;
import com.recurrence.contract.domain.enums.InvoiceStatus;

import java.time.OffsetDateTime;

/**
 * Cobrança de um ciclo da assinatura (PagBank {@code invoice} / Mercado Pago
 * {@code authorized_payment}).
 */
public final class Invoice {
    private final String id;
    private final InvoiceStatus status;
    private final Integer occurrence;
    private final Amount amount;
    private final String subscriptionId;
    private final PlanReference plan;
    private final CustomerReference customer;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    private Invoice(Builder builder) {
        this.id = builder.id;
        this.status = builder.status;
        this.occurrence = builder.occurrence;
        this.amount = builder.amount;
        this.subscriptionId = builder.subscriptionId;
        this.plan = builder.plan;
        this.customer = builder.customer;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    /** Número do ciclo da assinatura a que a fatura se refere. */
    public Integer getOccurrence() {
        return occurrence;
    }

    public Amount getAmount() {
        return amount;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public PlanReference getPlan() {
        return plan;
    }

    public CustomerReference getCustomer() {
        return customer;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static final class Builder {
        private String id;
        private InvoiceStatus status;
        private Integer occurrence;
        private Amount amount;
        private String subscriptionId;
        private PlanReference plan;
        private CustomerReference customer;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        private Builder() {}

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder status(InvoiceStatus status) {
            this.status = status;
            return this;
        }

        public Builder occurrence(Integer occurrence) {
            this.occurrence = occurrence;
            return this;
        }

        public Builder amount(Amount amount) {
            this.amount = amount;
            return this;
        }

        public Builder subscriptionId(String subscriptionId) {
            this.subscriptionId = subscriptionId;
            return this;
        }

        public Builder plan(PlanReference plan) {
            this.plan = plan;
            return this;
        }

        public Builder customer(CustomerReference customer) {
            this.customer = customer;
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

        public Invoice build() {
            return new Invoice(this);
        }
    }
}
