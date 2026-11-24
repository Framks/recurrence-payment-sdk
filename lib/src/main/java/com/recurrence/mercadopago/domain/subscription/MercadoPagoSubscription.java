package com.recurrence.mercadopago.domain.subscription;

import com.recurrence.mercadopago.domain.commons.MercadoPagoAutoRecurring;

import java.time.OffsetDateTime;

/**
 * Assinatura devolvida pelo Mercado Pago ({@code /preapproval}).
 *
 * <p>{@code nextPaymentDate} fica como texto e é convertido no mapper.</p>
 */
public record MercadoPagoSubscription(
        String id,
        String preapprovalPlanId,
        String reason,
        String externalReference,
        String payerId,
        String payerEmail,
        String status,
        MercadoPagoAutoRecurring autoRecurring,
        String nextPaymentDate,
        String paymentMethodId,
        String cardId,
        OffsetDateTime dateCreated,
        OffsetDateTime lastModified
) {
}
