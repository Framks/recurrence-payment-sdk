package com.recurrence.contract;

import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.request.TokenizeCardRequest;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.exception.RecurrenceException;

/**
 * Tokenização do cartão no backend: gera o {@link Card} pronto para usar como
 * meio de pagamento da assinatura.
 *
 * <p>No PagBank o cartão é criptografado localmente com a chave pública da conta
 * (mesmo resultado do {@code PagSeguro.encryptCard} do SDK JS); no Mercado Pago é
 * criado um {@code card_token} via {@code POST /v1/card_tokens}.</p>
 *
 * <p><b>PCI-DSS:</b> receber número e CVV no backend coloca a integração no escopo
 * completo do PCI-DSS. O fluxo recomendado pelos provedores é gerar o token no
 * frontend com o SDK JS de cada um; use este service em testes no sandbox ou se
 * o seu ambiente já for certificado.</p>
 *
 * <pre>{@code
 * Card card = recurrence.card().tokenize(TokenizeCardRequest.builder()
 *     .number("5555666677778884")
 *     .securityCode("123")
 *     .expMonth(12)
 *     .expYear(2030)
 *     .holderName("Joao Pereira Azul")
 *     .holderTaxId("12345678909")
 *     .build());
 * }</pre>
 */
public interface CardService {

    default Card tokenize(TokenizeCardRequest card) throws RecurrenceException {
        return tokenize(card, RequestOptions.empty());
    }

    Card tokenize(TokenizeCardRequest card, RequestOptions requestOptions) throws RecurrenceException;
}
