package com.recurrence.mercadopago.domain.subscription;

import com.recurrence.mercadopago.domain.commons.MercadoPagoAutoRecurring;

/**
 * Corpo de {@code POST /preapproval} para assinatura vinculada a plano e
 * cobrada em cartão ({@code status = authorized}).
 */
public record MercadoPagoSubscriptionRequest(
        String preapprovalPlanId,
        String externalReference,
        String payerEmail,
        String cardTokenId,
        MercadoPagoAutoRecurring autoRecurring,
        String backUrl,
        String status
) {
}
