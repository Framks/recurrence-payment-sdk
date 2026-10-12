package com.recurrence.mercadopago.domain.card;

/**
 * Corpo de {@code POST /v1/card_tokens} a partir dos dados abertos do cartão.
 */
public record MercadoPagoCardTokenRequest(
        String cardNumber,
        Integer expirationMonth,
        Integer expirationYear,
        String securityCode,
        Cardholder cardholder
) {
    public record Cardholder(String name, Identification identification) {
    }

    /** {@code type} é {@code CPF} ou {@code CNPJ}. */
    public record Identification(String type, String number) {
    }
}
