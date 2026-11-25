package com.recurrence.mercadopago.domain.payment;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/** Pagamento devolvido pelo Mercado Pago ({@code /v1/payments}). */
public record MercadoPagoPayment(
        String id,
        String status,
        String statusDetail,
        BigDecimal transactionAmount,
        String currencyId,
        String paymentTypeId,
        String paymentMethodId,
        Card card,
        Payer payer,
        OffsetDateTime dateCreated,
        OffsetDateTime dateLastUpdated
) {
    public record Card(
            String firstSixDigits,
            String lastFourDigits
    ) {
    }

    public record Payer(
            String id,
            String email
    ) {
    }
}
