package com.recurrence.contract.domain;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.CustomerReference;
import com.recurrence.contract.domain.commons.PlanReference;
import com.recurrence.contract.domain.enums.SubscriptionStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Assinatura devolvida pelo provedor.
 *
 * <p>Plano e assinante vêm como referências resumidas; use os respectivos
 * services para obter o objeto completo.</p>
 */
public final class Subscription {
    private final String id;
    private final String referenceId;
    private final SubscriptionStatus status;
    private final Amount amount;
    private final PlanReference plan;
    private final CustomerReference customer;
    private final BillingInfo paymentMethod;
    private final LocalDate nextInvoiceAt;
    private final LocalDate expAt;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    private Subscription(Builder builder) {
        this.id = builder.id;
        this.referenceId = builder.referenceId;
        this.status = builder.status;
        this.amount = builder.amount;
        this.plan = builder.plan;
        this.customer = builder.customer;
        this.paymentMethod = builder.paymentMethod;
        this.nextInvoiceAt = builder.nextInvoiceAt;
        this.expAt = builder.expAt;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public Amount getAmount() {
        return amount;
    }

    public PlanReference getPlan() {
        return plan;
    }

    public CustomerReference getCustomer() {
        return customer;
    }

    public BillingInfo getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDate getNextInvoiceAt() {
        return nextInvoiceAt;
    }

    public LocalDate getExpAt() {
        return expAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static final class Builder {
        private String id;
        private String referenceId;
        private SubscriptionStatus status;
        private Amount amount;
        private PlanReference plan;
        private CustomerReference customer;
        private BillingInfo paymentMethod;
        private LocalDate nextInvoiceAt;
        private LocalDate expAt;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        private Builder() {}

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder referenceId(String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public Builder status(SubscriptionStatus status) {
            this.status = status;
            return this;
        }

        public Builder amount(Amount amount) {
            this.amount = amount;
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

        public Builder paymentMethod(BillingInfo paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder nextInvoiceAt(LocalDate nextInvoiceAt) {
            this.nextInvoiceAt = nextInvoiceAt;
            return this;
        }

        public Builder expAt(LocalDate expAt) {
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

        public Subscription build() {
            return new Subscription(this);
        }
    }
}
