package com.recurrence.mercadopago.domain.plan;

import com.recurrence.mercadopago.domain.commons.MercadoPagoAutoRecurring;

import java.time.OffsetDateTime;

/** Plano devolvido pelo Mercado Pago ({@code /preapproval_plan}). */
public record MercadoPagoPlan(
        String id,
        String reason,
        String status,
        MercadoPagoAutoRecurring autoRecurring,
        MercadoPagoPaymentMethodsAllowed paymentMethodsAllowed,
        String backUrl,
        OffsetDateTime dateCreated,
        OffsetDateTime lastModified
) {
}
