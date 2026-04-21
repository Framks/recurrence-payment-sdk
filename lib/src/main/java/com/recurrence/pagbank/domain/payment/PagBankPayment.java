package com.recurrence.pagbank.domain.payment;

import com.recurrence.pagbank.domain.commons.PaymentMethod;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import java.time.OffsetDateTime;

public class PagBankPayment {
    private String id;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private PagBankInvoice invoice;
    private PagBankCustomer customer;
    private PaymentMethod paymentMethod;
    private Provider provider;

    public record Provider(
            String name,
            String transactionId,
            String code,
            String message,
            String reference
    ){}

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public PagBankInvoice getInvoice() {
        return invoice;
    }

    public PagBankCustomer getCustomer() {
        return customer;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Provider getProvider() {
        return provider;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setInvoice(PagBankInvoice invoice) {
        this.invoice = invoice;
    }

    public void setCustomer(PagBankCustomer customer) {
        this.customer = customer;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
