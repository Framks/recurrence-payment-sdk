package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.request.TokenizeCardRequest;

import java.text.Normalizer;

/**
 * Conversões do cartão para a criptografia do PagBank, reproduzindo o
 * {@code PagSeguro.encryptCard} do SDK JS.
 */
public final class PagBankCardMapper {

    private static final int HOLDER_MAX_LENGTH = 30;

    private PagBankCardMapper() {}

    /**
     * Texto cifrado pelo PagBank: {@code numero;cvv;MM;AAAA;titular;timestamp}.
     * O {@code holderTaxId} não faz parte do cartão criptografado e é ignorado.
     */
    public static String toPayload(TokenizeCardRequest card) {
        return String.join(";",
                card.getNumber().trim(),
                card.getSecurityCode().trim(),
                String.format("%02d", card.getExpMonth()),
                String.valueOf(card.getExpYear()),
                holder(card.getHolderName()),
                String.valueOf(System.currentTimeMillis()));
    }

    /** O CVV segue no {@code Card} porque o PagBank o exige junto do cartão criptografado. */
    public static Card toContract(String encryptedCard, TokenizeCardRequest card) {
        String number = card.getNumber().trim();
        return Card.builder()
                .token(encryptedCard)
                .securityCode(card.getSecurityCode().trim())
                .lastDigits(number.substring(number.length() - 4))
                .build();
    }

    /** Mesma normalização do SDK JS: até 30 caracteres, sem acentos, só letras e espaços. */
    private static String holder(String holderName) {
        String trimmed = holderName.trim();
        String truncated = trimmed.length() > HOLDER_MAX_LENGTH ? trimmed.substring(0, HOLDER_MAX_LENGTH) : trimmed;
        return Normalizer.normalize(truncated, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-zA-Z\\s]", "");
    }
}
