package com.recurrence.pagbank.domain.payment;

import com.recurrence.pagbank.domain.commons.PagBankAmount;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import com.recurrence.pagbank.domain.plan.PagBankPlan;
import com.recurrence.pagbank.domain.subscription.PagBankSubscription;
import java.time.OffsetDateTime;
import java.util.List;

public class PagBankInvoice {
    private String id;
    private String status;
    private Integer occurrence;
    private PagBankAmount amount;
    private PagBankSubscription subscription;
    private List<Item> items;
    private PagBankCustomer customer;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private PagBankPlan plan;

    public record Item(
           String type,
           PagBankAmount amount
    ){}

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

    public PagBankAmount getAmount() {
        return amount;
    }

    public PagBankSubscription getSubscription() {
        return subscription;
    }

    public List<Item> getItems() {
        return items;
    }

    public PagBankCustomer getCustomer() {
        return customer;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public PagBankPlan getPlan() {
        return plan;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOccurrence(Integer occurrence) {
        this.occurrence = occurrence;
    }

    public void setAmount(PagBankAmount amount) {
        this.amount = amount;
    }

    public void setSubscription(PagBankSubscription subscription) {
        this.subscription = subscription;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setCustomer(PagBankCustomer customer) {
        this.customer = customer;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setPlan(PagBankPlan plan) {
        this.plan = plan;
    }
}
