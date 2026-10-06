package com.recurrence.pagbank.domain.commons;

/**
 * Meio de pagamento no formato do PagBank. Usado em {@code customer.billing_info},
 * {@code subscription.payment_method} e {@code payment.payment_method}.
 */
public record PagBankBillingInfo(
        String type,
        PagBankCard card
) {
}
