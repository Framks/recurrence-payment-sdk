package com.recurrence.pagbank.domain.card;

/**
 * Resposta de {@code GET /public-keys}: chave RSA (X.509, Base64) usada para
 * criptografar o cartão.
 */
public record PagBankPublicKey(
        String publicKey,
        Long createdAt
) {
}
