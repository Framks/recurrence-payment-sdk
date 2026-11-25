package com.recurrence.mercadopago.domain.card;

/**
 * Resposta de {@code POST /v1/card_tokens}. O {@code id} é o {@code card_token_id},
 * de uso único e com validade curta.
 */
public record MercadoPagoCardToken(
        String id,
        String firstSixDigits,
        String lastFourDigits,
        Integer expirationMonth,
        Integer expirationYear,
        String status
) {
}
