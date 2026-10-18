package com.recurrence.contract;

import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.request.CreatePlanRequest;
import com.recurrence.contract.domain.request.UpdatePlanRequest;
import com.recurrence.contract.filters.PlanFilter;
import com.recurrence.contract.filters.PageOptions;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.exception.RecurrenceException;

/**
 * Operações de plano comuns aos provedores (PagBank {@code /plans}, Mercado Pago
 * {@code /preapproval_plan}). Obtido por {@link Recurrence#plan()}.
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
public interface PlanService {

    /**
     * Cria um plano com as opções padrão.
     *
     * @param plan dados do plano; {@code name}, {@code amount} e {@code interval} são obrigatórios
     * @return o plano criado, com o {@code id} atribuído pelo provedor
     * @throws RecurrenceException se o pedido for inválido ou o provedor recusar
     * @see #create(CreatePlanRequest, RequestOptions)
     */
    default Plan create(CreatePlanRequest plan) throws RecurrenceException {
        return create(plan, RequestOptions.empty());
    }

    /**
     * Cria um plano. Sem {@code paymentMethods}, o plano aceita só cartão de crédito.
     *
     * @param plan           dados do plano; {@code name}, {@code amount} e {@code interval} são obrigatórios
     * @param requestOptions chave de idempotência e timeout desta chamada
     * @return o plano criado, com o {@code id} atribuído pelo provedor
     * @throws com.recurrence.exception.RecurrenceValidationException se faltar campo obrigatório,
     *         o valor não for positivo ou {@code paymentMethods} tiver algo além de {@code CREDIT_CARD}
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    Plan create(CreatePlanRequest plan, RequestOptions requestOptions) throws RecurrenceException;

    /**
     * Consulta um plano pelo id.
     *
     * @param planId id do plano no provedor
     * @return o plano
     * @throws com.recurrence.exception.RecurrenceValidationException se {@code planId} estiver em branco
     * @throws com.recurrence.exception.RecurrenceApiException com categoria {@code NOT_FOUND} se o plano não existir
     */
    Plan get(String planId) throws RecurrenceException;

    /**
     * Lista planos sem filtro.
     *
     * @param pageOptions paginação; {@code null} usa o padrão do provedor
     * @return uma página de planos
     * @throws RecurrenceException se o provedor recusar a busca
     * @see #list(PageOptions, PlanFilter)
     */
    default PageResponse<Plan> list(PageOptions pageOptions) throws RecurrenceException {
        return list(pageOptions, null);
    }

    /**
     * Lista planos com filtro.
     *
     * @param pageOptions paginação; {@code null} usa o padrão do provedor
     * @param planFilter  filtros; {@code null} não filtra
     * @return uma página de planos
     * @throws com.recurrence.exception.RecurrenceValidationException se o filtro usar o status {@code UNKNOWN}
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    PageResponse<Plan> list(PageOptions pageOptions, PlanFilter planFilter) throws RecurrenceException;

    /**
     * Atualiza um plano com as opções padrão.
     *
     * @param planId id do plano no provedor
     * @param plan   campos a alterar; campo nulo não é alterado
     * @return o plano atualizado
     * @throws RecurrenceException se o pedido for inválido ou o provedor recusar
     * @see #update(String, UpdatePlanRequest, RequestOptions)
     */
    default Plan update(String planId, UpdatePlanRequest plan) throws RecurrenceException {
        return update(planId, plan, RequestOptions.empty());
    }

    /**
     * Atualiza um plano. Só os campos informados são enviados ao provedor.
     *
     * @param planId         id do plano no provedor
     * @param plan           campos a alterar; campo nulo não é alterado, e ao menos um deve ser informado
     * @param requestOptions chave de idempotência e timeout desta chamada
     * @return o plano atualizado
     * @throws com.recurrence.exception.RecurrenceValidationException se nenhum campo for informado ou algum for inválido
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    Plan update(String planId, UpdatePlanRequest plan, RequestOptions requestOptions) throws RecurrenceException;

    /**
     * Ativa um plano com as opções padrão.
     *
     * @param planId id do plano no provedor
     * @throws RecurrenceException se o provedor recusar
     * @see #activate(String, RequestOptions)
     */
    default void activate(String planId) throws RecurrenceException {
        activate(planId, RequestOptions.empty());
    }

    /**
     * Ativa um plano inativo (PagBank {@code PUT /plans/{id}/activate}; Mercado Pago
     * {@code status = active}).
     *
     * @param planId         id do plano no provedor
     * @param requestOptions chave de idempotência e timeout desta chamada
     * @throws com.recurrence.exception.RecurrenceValidationException se {@code planId} estiver em branco
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    void activate(String planId, RequestOptions requestOptions) throws RecurrenceException;

    /**
     * Inativa um plano com as opções padrão.
     *
     * @param planId id do plano no provedor
     * @throws RecurrenceException se o provedor recusar
     * @see #deactivate(String, RequestOptions)
     */
    default void deactivate(String planId) throws RecurrenceException {
        deactivate(planId, RequestOptions.empty());
    }

    /**
     * Inativa um plano: ele deixa de aceitar novas assinaturas (PagBank
     * {@code PUT /plans/{id}/inactivate}; Mercado Pago {@code status = cancelled}).
     * Nos dois provedores o plano pode ser reativado com {@link #activate(String)}.
     *
     * @param planId         id do plano no provedor
     * @param requestOptions chave de idempotência e timeout desta chamada
     * @throws com.recurrence.exception.RecurrenceValidationException se {@code planId} estiver em branco
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    void deactivate(String planId, RequestOptions requestOptions) throws RecurrenceException;
}
