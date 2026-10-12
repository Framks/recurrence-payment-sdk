package com.recurrence.pagbank.domain.subscription;

import com.recurrence.pagbank.domain.commons.PagBankAmount;
import com.recurrence.pagbank.domain.commons.PagBankBillingInfo;
import com.recurrence.pagbank.domain.commons.PagBankIdRef;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;

import java.util.List;

/**
 * Corpo de criação de assinatura no PagBank.
 *
 * <p>O cartão criptografado vai em {@code customer.billing_info}; em
 * {@code payment_method} vai apenas o CVV.</p>
 */
public record PagBankSubscriptionRequest(
        String referenceId,
        PagBankIdRef plan,
        PagBankAmount amount,
        PagBankCustomer customer,
        List<PagBankBillingInfo> paymentMethod
) {
}
