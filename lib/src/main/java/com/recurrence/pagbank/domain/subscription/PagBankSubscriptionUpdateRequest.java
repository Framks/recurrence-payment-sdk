package com.recurrence.pagbank.domain.subscription;

import com.recurrence.pagbank.domain.commons.PagBankAmount;
import com.recurrence.pagbank.domain.commons.PagBankIdRef;

/** Corpo de {@code PUT /subscriptions/{id}}. Campos nulos não são enviados. */
public record PagBankSubscriptionUpdateRequest(
        PagBankIdRef plan,
        PagBankAmount amount
) {
}
