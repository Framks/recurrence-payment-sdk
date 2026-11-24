package com.recurrence.mercadopago.mapper;

import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.request.TokenizeCardRequest;
import com.recurrence.mercadopago.domain.card.MercadoPagoCardToken;
import com.recurrence.mercadopago.domain.card.MercadoPagoCardTokenRequest;

/**
 * Conversões entre o contrato e {@code /v1/card_tokens}.
 */
public final class MercadoPagoCardMapper {

    private static final int CPF_LENGTH = 11;

    private MercadoPagoCardMapper() {}

    /** O tipo do documento é deduzido pelo tamanho: 11 dígitos é CPF, 14 é CNPJ. */
    public static MercadoPagoCardTokenRequest toRequest(TokenizeCardRequest card) {
        String taxId = card.getHolderTaxId();
        return new MercadoPagoCardTokenRequest(
                card.getNumber().trim(),
                card.getExpMonth(),
                card.getExpYear(),
                card.getSecurityCode().trim(),
                new MercadoPagoCardTokenRequest.Cardholder(
                        card.getHolderName().trim(),
                        new MercadoPagoCardTokenRequest.Identification(
                                taxId.length() == CPF_LENGTH ? "CPF" : "CNPJ",
                                taxId
                        )
                )
        );
    }

    /** O CVV já vai dentro do token, por isso {@code securityCode} fica vazio. */
    public static Card toContract(MercadoPagoCardToken token) {
        return Card.builder()
                .token(token.id())
                .lastDigits(token.lastFourDigits())
                .build();
    }
}
