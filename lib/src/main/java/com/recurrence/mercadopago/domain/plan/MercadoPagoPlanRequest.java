package com.recurrence.mercadopago.domain.plan;

import com.recurrence.mercadopago.domain.commons.MercadoPagoAutoRecurring;

/**
 * Corpo de criação/atualização de plano. Campos nulos não são enviados; o
 * Mercado Pago aceita atualização parcial (confirmado no sandbox).
 */
public record MercadoPagoPlanRequest(
        String reason,
        MercadoPagoAutoRecurring autoRecurring,
        MercadoPagoPaymentMethodsAllowed paymentMethodsAllowed,
        String backUrl
) {
}
