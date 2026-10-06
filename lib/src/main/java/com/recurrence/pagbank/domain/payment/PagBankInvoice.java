package com.recurrence.pagbank.domain.payment;

import com.recurrence.pagbank.domain.commons.PagBankAmount;
import com.recurrence.pagbank.domain.commons.PagBankIdRef;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import com.recurrence.pagbank.domain.plan.PagBankPlan;

import java.time.OffsetDateTime;

/** Fatura devolvida pelo PagBank ({@code /invoices}). */
public record PagBankInvoice(
        String id,
        String status,
        Integer occurrence,
        PagBankAmount amount,
        PagBankIdRef subscription,
        PagBankPlan plan,
        PagBankCustomer customer,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
