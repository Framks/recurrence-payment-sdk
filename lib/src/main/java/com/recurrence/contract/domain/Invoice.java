package com.recurrence.contract.domain;

import com.recurrence.contract.domain.commons.Amount;
import java.time.OffsetDateTime;
import java.util.List;

public class Invoice {
    private String id;
    private String status;
    private Integer occurrence;
    private Amount amount;
    private Subscription subscription;
    private List<Item> items;
    private Customer customer;
    private Plan plan;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public record Item(
            String type,
            Amount amount
    ) {}

    public Invoice() {}

    private Invoice(Builder builder) {
        this.id = builder.id;
        this.status = builder.status;
        this.occurrence = builder.occurrence;
        this.amount = builder.amount;
        this.subscription = builder.subscription;
        this.items = builder.items;
        this.customer = builder.customer;
        this.plan = builder.plan;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }
    public Integer getOccurrence() {
        return occurrence;
    }
    public Amount getAmount() {
        return amount;
    }
    public Subscription getSubscription() {
        return subscription;
    }
    public List<Item> getItems() {
        return items;
    }
    public Customer getCustomer() {
        return customer;
    }
    public Plan getPlan() {
        return plan;
    }
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Builder
    public static class Builder {
        private String id;
        private String status;
        private Integer occurrence;
        private Amount amount;
        private Subscription subscription;
        private List<Item> items;
        private Customer customer;
        private Plan plan;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder status(String status) {
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

        public Builder subscription(Subscription subscription) {
            this.subscription = subscription;
            return this;
        }

        public Builder items(List<Item> items) {
            this.items = items;
            return this;
        }

        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder plan(Plan plan) {
            this.plan = plan;
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
