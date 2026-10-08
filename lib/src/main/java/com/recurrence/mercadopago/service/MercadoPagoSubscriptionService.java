package com.recurrence.mercadopago.service;

import com.recurrence.contract.SubscriptionService;
import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.domain.request.CreateSubscriptionRequest;
import com.recurrence.contract.domain.request.UpdateSubscriptionRequest;
import com.recurrence.contract.filters.PageOptions;
import com.recurrence.contract.filters.SubscriptionFilter;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.core.BaseService;
import com.recurrence.core.ProviderContext;
import com.recurrence.core.QueryParams;
import com.recurrence.core.RequestValidator;
import com.recurrence.exception.RecurrenceValidationException;
import com.recurrence.mercadopago.domain.commons.MercadoPagoStatusRequest;
import com.recurrence.mercadopago.domain.subscription.MercadoPagoSubscription;
import com.recurrence.mercadopago.domain.subscription.MercadoPagoSubscriptionList;
import com.recurrence.mercadopago.mapper.MercadoPagoFilterMapper;
import com.recurrence.mercadopago.mapper.MercadoPagoSubscriptionMapper;

import static com.recurrence.constants.ValidationsMessage.MERCADO_PAGO_PLAN_CHANGE_UNSUPPORTED;
import static com.recurrence.infrastructure.MethodRequest.GET;
import static com.recurrence.infrastructure.MethodRequest.POST;
import static com.recurrence.infrastructure.MethodRequest.PUT;
import static com.recurrence.mercadopago.constant.Constant.EXTERNAL_REFERENCE;
import static com.recurrence.mercadopago.constant.Constant.Operation;
import static com.recurrence.mercadopago.constant.Constant.PATH_SEARCH;
import static com.recurrence.mercadopago.constant.Constant.QUERY;
import static com.recurrence.mercadopago.constant.Constant.STATUS;
import static com.recurrence.mercadopago.constant.Constant.SUBSCRIPTIONS;
import static com.recurrence.mercadopago.constant.Constant.SUBSCRIPTION_ID;
import static com.recurrence.mercadopago.constant.Constant.Status.SUBSCRIPTION_AUTHORIZED;
import static com.recurrence.mercadopago.constant.Constant.Status.SUBSCRIPTION_CANCELLED;
import static com.recurrence.mercadopago.constant.Constant.Status.SUBSCRIPTION_PAUSED;

/**
 * Assinaturas no Mercado Pago ({@code /preapproval}).
 *
 * <p>No sandbox, vendedor (dono do token) e assinante precisam ser ambos usuários
 * de teste, ou ambos reais; caso contrário o Mercado Pago recusa a criação.</p>
 */
public class MercadoPagoSubscriptionService extends BaseService implements SubscriptionService {

    public MercadoPagoSubscriptionService(ProviderContext context) {
        super(context);
    }

    @Override
    public Subscription create(CreateSubscriptionRequest subscription, RequestOptions requestOptions) {
        RequestValidator.validate(subscription);
        return execute(
                Operation.SUBSCRIPTION_CREATE,
                request(POST, SUBSCRIPTIONS, requestOptions)
                        .body(toJson(Operation.SUBSCRIPTION_CREATE,
                                MercadoPagoSubscriptionMapper.toRequest(subscription, context.config().getBackUrl())))
                        .build(),
                MercadoPagoSubscription.class,
                MercadoPagoSubscriptionMapper::toContract
        );
    }

    @Override
    public Subscription get(String subscriptionId) {
        RequestValidator.requireId(subscriptionId, SUBSCRIPTION_ID);
        return execute(
                Operation.SUBSCRIPTION_GET,
                request(GET, subscriptionPath(subscriptionId), null).build(),
                MercadoPagoSubscription.class,
                MercadoPagoSubscriptionMapper::toContract
        );
    }

    @Override
    public PageResponse<Subscription> list(PageOptions pageOptions, SubscriptionFilter subscriptionFilter) {
        QueryParams query = QueryParams.create().page(pageOptions);
        if (subscriptionFilter != null) {
            query.add(STATUS, MercadoPagoFilterMapper.subscriptionStatus(subscriptionFilter.getStatus()))
                    .add(QUERY, subscriptionFilter.getQuery())
                    .add(EXTERNAL_REFERENCE, subscriptionFilter.getReferenceId());
        }
        return execute(
                Operation.SUBSCRIPTION_LIST,
                request(GET, query.appendTo(SUBSCRIPTIONS + PATH_SEARCH), null).build(),
                MercadoPagoSubscriptionList.class,
                MercadoPagoSubscriptionMapper::toPage
        );
    }

    /**
     * Valor e cartão ({@code card_token_id}) são alterados no mesmo
     * {@code PUT /preapproval/{id}}. Ao final devolve a assinatura atualizada.
     *
     * <p>Troca de plano é recusada: no sandbox o Mercado Pago responde 2xx ao
     * {@code preapproval_plan_id} mas mantém o plano antigo, o que seria um
     * sucesso falso para o integrador.</p>
     */
    @Override
    public Subscription update(String subscriptionId, UpdateSubscriptionRequest subscription, RequestOptions requestOptions) {
        RequestValidator.requireId(subscriptionId, SUBSCRIPTION_ID);
        RequestValidator.validate(subscription);
        if (subscription.getPlanId() != null) {
            throw new RecurrenceValidationException(MERCADO_PAGO_PLAN_CHANGE_UNSUPPORTED);
        }
        execute(
                Operation.SUBSCRIPTION_UPDATE,
                request(PUT, subscriptionPath(subscriptionId), requestOptions)
                        .body(toJson(Operation.SUBSCRIPTION_UPDATE, MercadoPagoSubscriptionMapper.toRequest(subscription)))
                        .build()
        );
        return get(subscriptionId);
    }

    @Override
    public void suspend(String subscriptionId, RequestOptions requestOptions) {
        changeStatus(Operation.SUBSCRIPTION_SUSPEND, subscriptionId, SUBSCRIPTION_PAUSED, requestOptions);
    }

    @Override
    public void cancel(String subscriptionId, RequestOptions requestOptions) {
        changeStatus(Operation.SUBSCRIPTION_CANCEL, subscriptionId, SUBSCRIPTION_CANCELLED, requestOptions);
    }

    @Override
    public void activate(String subscriptionId, RequestOptions requestOptions) {
        changeStatus(Operation.SUBSCRIPTION_ACTIVATE, subscriptionId, SUBSCRIPTION_AUTHORIZED, requestOptions);
    }

    /** O Mercado Pago não tem endpoint de ação: o estado muda pelo {@code PUT} da assinatura. */
    private void changeStatus(String operation, String subscriptionId, String status, RequestOptions requestOptions) {
        RequestValidator.requireId(subscriptionId, SUBSCRIPTION_ID);
        execute(
                operation,
                request(PUT, subscriptionPath(subscriptionId), requestOptions)
                        .body(toJson(operation, new MercadoPagoStatusRequest(status)))
                        .build()
        );
    }

    private static String subscriptionPath(String subscriptionId) {
        return SUBSCRIPTIONS + "/" + segment(subscriptionId);
    }
}
