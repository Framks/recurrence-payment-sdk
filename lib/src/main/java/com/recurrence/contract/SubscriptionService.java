package com.recurrence.contract;

import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.domain.request.CreateSubscriptionRequest;
import com.recurrence.contract.domain.request.UpdateSubscriptionRequest;
import com.recurrence.contract.filters.PageOptions;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.contract.filters.SubscriptionFilter;
import com.recurrence.exception.RecurrenceException;

/**
 * Operações de assinatura comuns aos provedores (PagBank {@code /subscriptions},
 * Mercado Pago {@code /preapproval}). Obtido por {@link Recurrence#subscription()}.
 *
 * <p>Cada operação tem um único método abstrato; as variações sem
 * {@link RequestOptions} ou sem filtro são {@code default}.</p>
 *
 * <p>Todos os métodos validam a entrada antes de qualquer chamada HTTP e lançam
 * subclasses de {@link RecurrenceException} (não checadas):
 * {@code RecurrenceValidationException} para pedido inválido,
 * {@code RecurrenceApiException} quando o provedor responde fora de 2xx e
 * {@code RecurrenceNetworkException} quando não há resposta.</p>
 */
public interface SubscriptionService {

    /**
     * Cria uma assinatura com as opções padrão.
     *
     * @param subscription dados da assinatura
     * @return a assinatura criada
     * @throws RecurrenceException se o pedido for inválido ou o provedor recusar
     * @see #create(CreateSubscriptionRequest, RequestOptions)
     */
    default Subscription create(CreateSubscriptionRequest subscription) throws RecurrenceException {
        return create(subscription, RequestOptions.empty());
    }

    /**
     * Cria uma assinatura vinculada a um plano e cobrada em cartão de crédito.
     *
     * <p>Obrigatórios nos dois provedores: {@code planId}, {@code customer.email} e
     * {@code paymentMethod} com {@code card.token}. O PagBank exige também
     * {@code referenceId}, {@code customer.name}, {@code customer.taxId},
     * {@code customer.phone} e {@code card.securityCode}. O Mercado Pago usa só o
     * e-mail do assinante e o token do cartão.</p>
     *
     * @param subscription   dados da assinatura
     * @param requestOptions chave de idempotência e timeout desta chamada
     * @return a assinatura criada
     * @throws com.recurrence.exception.RecurrenceValidationException se faltar campo obrigatório para o provedor ativo
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    Subscription create(CreateSubscriptionRequest subscription, RequestOptions requestOptions) throws RecurrenceException;

    /**
     * Consulta uma assinatura pelo id.
     *
     * @param subscriptionId id da assinatura no provedor
     * @return a assinatura, com plano e assinante como referências resumidas
     * @throws com.recurrence.exception.RecurrenceValidationException se {@code subscriptionId} estiver em branco
     * @throws com.recurrence.exception.RecurrenceApiException com categoria {@code NOT_FOUND} se a assinatura não existir
     */
    Subscription get(String subscriptionId) throws RecurrenceException;

    /**
     * Lista assinaturas sem filtro.
     *
     * @param pageOptions paginação; {@code null} usa o padrão do provedor
     * @return uma página de assinaturas
     * @throws RecurrenceException se o provedor recusar a busca
     * @see #list(PageOptions, SubscriptionFilter)
     */
    default PageResponse<Subscription> list(PageOptions pageOptions) throws RecurrenceException {
        return list(pageOptions, null);
    }

    /**
     * Lista assinaturas com filtro.
     *
     * @param pageOptions        paginação; {@code null} usa o padrão do provedor
     * @param subscriptionFilter filtros; {@code null} não filtra
     * @return uma página de assinaturas
     * @throws com.recurrence.exception.RecurrenceValidationException se o status do filtro for {@code UNKNOWN}
     *         ou não existir no provedor ativo ({@code OVERDUE} e {@code EXPIRED} no Mercado Pago)
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    PageResponse<Subscription> list(PageOptions pageOptions, SubscriptionFilter subscriptionFilter) throws RecurrenceException;

    /**
     * Atualiza uma assinatura com as opções padrão.
     *
     * @param subscriptionId id da assinatura no provedor
     * @param subscription   campos a alterar; campo nulo não é alterado
     * @return a assinatura atualizada
     * @throws RecurrenceException se o pedido for inválido ou o provedor recusar
     * @see #update(String, UpdateSubscriptionRequest, RequestOptions)
     */
    default Subscription update(String subscriptionId, UpdateSubscriptionRequest subscription) throws RecurrenceException {
        return update(subscriptionId, subscription, RequestOptions.empty());
    }

    /**
     * Atualiza plano, valor ou cartão de uma assinatura e devolve a assinatura
     * consultada de novo após a alteração.
     *
     * <p>A assinatura devolvida mostra o que o provedor de fato gravou: no sandbox do
     * Mercado Pago a troca de plano responde 2xx mas mantém o plano antigo, então
     * confira {@code getPlan()} no retorno. No PagBank a troca de cartão
     * vai para {@code PUT /customers/{id}/billing_info} e exige
     * {@code card.securityCode}.</p>
     *
     * @param subscriptionId id da assinatura no provedor
     * @param subscription   campos a alterar; campo nulo não é alterado, e ao menos um deve ser informado
     * @param requestOptions chave de idempotência e timeout desta chamada
     * @return a assinatura atualizada
     * @throws com.recurrence.exception.RecurrenceValidationException se nenhum campo for informado ou algum
     *         for inválido
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    Subscription update(String subscriptionId, UpdateSubscriptionRequest subscription, RequestOptions requestOptions) throws RecurrenceException;

    /**
     * Suspende uma assinatura com as opções padrão.
     *
     * @param subscriptionId id da assinatura no provedor
     * @throws RecurrenceException se o provedor recusar
     * @see #suspend(String, RequestOptions)
     */
    default void suspend(String subscriptionId) throws RecurrenceException {
        suspend(subscriptionId, RequestOptions.empty());
    }

    /**
     * Suspende (pausa) as cobranças de uma assinatura (PagBank
     * {@code PUT /subscriptions/{id}/suspend}; Mercado Pago {@code status = paused}).
     *
     * @param subscriptionId id da assinatura no provedor
     * @param requestOptions chave de idempotência e timeout desta chamada
     * @throws com.recurrence.exception.RecurrenceValidationException se {@code subscriptionId} estiver em branco
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    void suspend(String subscriptionId, RequestOptions requestOptions) throws RecurrenceException;

    /**
     * Cancela uma assinatura com as opções padrão.
     *
     * @param subscriptionId id da assinatura no provedor
     * @throws RecurrenceException se o provedor recusar
     * @see #cancel(String, RequestOptions)
     */
    default void cancel(String subscriptionId) throws RecurrenceException {
        cancel(subscriptionId, RequestOptions.empty());
    }

    /**
     * Cancela uma assinatura de forma definitiva (PagBank
     * {@code PUT /subscriptions/{id}/cancel}; Mercado Pago {@code status = cancelled}).
     *
     * @param subscriptionId id da assinatura no provedor
     * @param requestOptions chave de idempotência e timeout desta chamada
     * @throws com.recurrence.exception.RecurrenceValidationException se {@code subscriptionId} estiver em branco
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    void cancel(String subscriptionId, RequestOptions requestOptions) throws RecurrenceException;

    /**
     * Reativa uma assinatura com as opções padrão.
     *
     * @param subscriptionId id da assinatura no provedor
     * @throws RecurrenceException se o provedor recusar
     * @see #activate(String, RequestOptions)
     */
    default void activate(String subscriptionId) throws RecurrenceException {
        activate(subscriptionId, RequestOptions.empty());
    }

    /**
     * Reativa uma assinatura suspensa (PagBank {@code PUT /subscriptions/{id}/activate};
     * Mercado Pago {@code status = authorized}).
     *
     * @param subscriptionId id da assinatura no provedor
     * @param requestOptions chave de idempotência e timeout desta chamada
     * @throws com.recurrence.exception.RecurrenceValidationException se {@code subscriptionId} estiver em branco
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    void activate(String subscriptionId, RequestOptions requestOptions) throws RecurrenceException;
}
