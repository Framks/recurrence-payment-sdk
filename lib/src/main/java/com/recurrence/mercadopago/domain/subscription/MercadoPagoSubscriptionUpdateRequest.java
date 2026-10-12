package com.recurrence.mercadopago.domain.subscription;

import com.recurrence.mercadopago.domain.commons.MercadoPagoAutoRecurring;

/** Corpo de {@code PUT /preapproval/{id}}. Campos nulos não são enviados. */
public record MercadoPagoSubscriptionUpdateRequest(
        String preapprovalPlanId,
        String cardTokenId,
        MercadoPagoAutoRecurring autoRecurring
) {
}
