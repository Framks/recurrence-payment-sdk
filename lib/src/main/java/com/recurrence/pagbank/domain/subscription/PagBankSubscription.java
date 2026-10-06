package com.recurrence.pagbank.domain.subscription;

import com.recurrence.pagbank.domain.commons.PagBankAmount;
import com.recurrence.pagbank.domain.commons.PagBankBillingInfo;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import com.recurrence.pagbank.domain.plan.PagBankPlan;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Assinatura devolvida pelo PagBank ({@code /subscriptions}).
 *
 * <p>{@code nextInvoiceAt} e {@code expAt} ficam como texto e são convertidos
 * no mapper, que tolera data ou data/hora.</p>
 */
public record PagBankSubscription(
        String id,
        String referenceId,
        String status,
        PagBankAmount amount,
        PagBankPlan plan,
        PagBankCustomer customer,
        List<PagBankBillingInfo> paymentMethod,
        String nextInvoiceAt,
        String expAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
