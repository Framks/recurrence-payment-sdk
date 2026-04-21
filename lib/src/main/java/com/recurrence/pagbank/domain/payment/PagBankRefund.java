package com.recurrence.pagbank.domain.payment;

import com.recurrence.pagbank.domain.commons.PagBankAmount;
import java.time.OffsetDateTime;

public class PagBankRefund {
    private String id;
    private String status;
    private String type;
    private OffsetDateTime createdAt;
    private PaymentRefund payment;
    private PagBankAmount amount;

    public record PaymentRefund(
            String id,
            PagBankAmount amount
    ){}

    // Getters
    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public PaymentRefund getPayment() {
        return payment;
    }

    public PagBankAmount getAmount() {
        return amount;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setPayment(PaymentRefund payment) {
        this.payment = payment;
    }

    public void setAmount(PagBankAmount amount) {
        this.amount = amount;
    }
}
