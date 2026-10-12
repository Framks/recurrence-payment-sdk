package com.recurrence.mercadopago.service;

import com.recurrence.contract.CardService;
import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.request.TokenizeCardRequest;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.core.BaseService;
import com.recurrence.core.ProviderContext;
import com.recurrence.core.RequestValidator;
import com.recurrence.mercadopago.domain.card.MercadoPagoCardToken;
import com.recurrence.mercadopago.mapper.MercadoPagoCardMapper;
import com.recurrence.mercadopago.validator.CardValidator;

import static com.recurrence.infrastructure.MethodRequest.POST;
import static com.recurrence.mercadopago.constant.Constant.CARD_TOKENS;
import static com.recurrence.mercadopago.constant.Constant.Operation.CARD_TOKENIZE;

/**
 * Cria o {@code card_token} via {@code POST /v1/card_tokens}, como o
 * {@code CardTokenClient} do SDK Java oficial (autenticado com o access token).
 */
public class MercadoPagoCardService extends BaseService implements CardService {

    public MercadoPagoCardService(ProviderContext context) {
        super(context);
    }

    @Override
    public Card tokenize(TokenizeCardRequest card, RequestOptions requestOptions) {
        RequestValidator.validate(card);
        CardValidator.validate(card);
        return execute(
                CARD_TOKENIZE,
                request(POST, CARD_TOKENS, requestOptions)
                        .body(toJson(CARD_TOKENIZE, MercadoPagoCardMapper.toRequest(card)))
                        .build(),
                MercadoPagoCardToken.class,
                MercadoPagoCardMapper::toContract
        );
    }
}
