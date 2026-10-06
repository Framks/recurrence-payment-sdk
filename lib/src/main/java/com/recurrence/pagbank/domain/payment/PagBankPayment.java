package com.recurrence.pagbank.domain.payment;

import com.recurrence.pagbank.domain.commons.PagBankBillingInfo;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;

import java.time.OffsetDateTime;

/** Pagamento (tentativa de cobrança) devolvido pelo PagBank ({@code /payments}). */
public record PagBankPayment(
        String id,
        String status,
        PagBankInvoice invoice,
        PagBankCustomer customer,
        PagBankBillingInfo paymentMethod,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
