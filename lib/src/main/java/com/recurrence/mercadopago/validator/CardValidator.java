package com.recurrence.mercadopago.validator;

import com.recurrence.contract.config.Provider;
import com.recurrence.contract.domain.request.TokenizeCardRequest;
import com.recurrence.exception.RecurrenceValidationException;

import static com.recurrence.constants.ValidationsMessage.CARD_HOLDER_TAXID_NULL;

/**
 * Regra do Mercado Pago para tokenizar o cartão, aplicada depois do
 * {@code RequestValidator} comum: o documento do titular é obrigatório.
 */
public final class CardValidator {

    private CardValidator() {}

    public static void validate(TokenizeCardRequest card) {
        if (card.getHolderTaxId() == null || card.getHolderTaxId().isBlank()) {
            throw new RecurrenceValidationException(String.format(CARD_HOLDER_TAXID_NULL, Provider.MERCADO_PAGO));
        }
    }
}
