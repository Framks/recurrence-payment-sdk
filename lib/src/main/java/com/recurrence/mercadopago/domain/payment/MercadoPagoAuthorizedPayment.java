package com.recurrence.mercadopago.domain.payment;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Fatura (cobrança de um ciclo) no Mercado Pago ({@code /authorized_payments}).
 *
 * <p>{@code payment} referencia o pagamento em {@code /v1/payments}, quando já houve tentativa.
 * O {@code id} só é atribuído alguns segundos depois da cobrança; até lá a busca
 * devolve a fatura sem id (observado no sandbox).</p>
 */
public record MercadoPagoAuthorizedPayment(
        String id,
        String preapprovalId,
        String payerId,
        String status,
        BigDecimal transactionAmount,
        String currencyId,
        Integer retryAttempt,
        OffsetDateTime debitDate,
        OffsetDateTime dateCreated,
        OffsetDateTime lastModified,
        PaymentRef payment
) {
    public record PaymentRef(
            String id,
            String status,
            String statusDetail
    ) {
    }
}
