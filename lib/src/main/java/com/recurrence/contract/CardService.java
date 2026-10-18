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

    /**
     * Tokeniza um cartão com as opções padrão.
     *
     * @param card dados abertos do cartão
     * @return o cartão pronto para usar em {@code BillingInfo}
     * @throws RecurrenceException se os dados forem inválidos ou o provedor recusar
     * @see #tokenize(TokenizeCardRequest, RequestOptions)
     */
    default Card tokenize(TokenizeCardRequest card) throws RecurrenceException {
        return tokenize(card, RequestOptions.empty());
    }

    /**
     * Tokeniza um cartão para o provedor ativo.
     *
     * <p>No PagBank o {@link Card} devolvido traz o cartão criptografado em
     * {@code token}, o CVV em {@code securityCode} e os 4 últimos dígitos; a chave
     * pública vem de {@code ProviderConfig.publicKey} ou é consultada uma vez em
     * {@code GET /public-keys}. No Mercado Pago traz o {@code card_token} (uso único,
     * validade curta) em {@code token} e os 4 últimos dígitos; o CVV já vai dentro do
     * token. {@code holderTaxId} é obrigatório só no Mercado Pago.</p>
     *
     * @param card           dados abertos do cartão
     * @param requestOptions chave de idempotência e timeout desta chamada
     * @return o cartão pronto para usar em {@code BillingInfo}
     * @throws com.recurrence.exception.RecurrenceValidationException se número, CVV, validade, titular ou
     *         documento forem inválidos, ou se faltar {@code holderTaxId} no Mercado Pago
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx (no PagBank,
     *         também quando a conta ainda não tem chave pública)
     * @throws com.recurrence.exception.RecurrenceSerializationException se a chave pública do PagBank for
     *         inválida ou a criptografia falhar
     */
    Card tokenize(TokenizeCardRequest card, RequestOptions requestOptions) throws RecurrenceException;
}
