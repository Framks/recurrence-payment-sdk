package com.recurrence.pagbank.service;

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
import com.recurrence.pagbank.domain.subscription.PagBankSubscription;
import com.recurrence.pagbank.domain.subscription.PagbankListSubscription;
import com.recurrence.pagbank.mapper.PagBankFilterMapper;
import com.recurrence.pagbank.mapper.PagBankSubscriptionMapper;
import com.recurrence.pagbank.validator.SubscriptionValidator;

import java.lang.ref.Reference;

import static com.recurrence.infrastructure.MethodRequest.GET;
import static com.recurrence.infrastructure.MethodRequest.POST;
import static com.recurrence.infrastructure.MethodRequest.PUT;
import static com.recurrence.pagbank.constant.Constant.Operation.SUBSCRIPTION_ACTIVATE;
import static com.recurrence.pagbank.constant.Constant.Operation.SUBSCRIPTION_CANCEL;
import static com.recurrence.pagbank.constant.Constant.Operation.SUBSCRIPTION_CREATE;
import static com.recurrence.pagbank.constant.Constant.Operation.SUBSCRIPTION_LIST;
import static com.recurrence.pagbank.constant.Constant.Operation.SUBSCRIPTION_SUSPEND;
import static com.recurrence.pagbank.constant.Constant.Operation.SUBSCRIPTION_UPDATE;
import static com.recurrence.pagbank.constant.Constant.Operation.SUBSCRIPTION_UPDATE_BILLING;
import static com.recurrence.pagbank.constant.Constant.PATH_ACTIVE;
import static com.recurrence.pagbank.constant.Constant.PATH_BILLING_INFO;
import static com.recurrence.pagbank.constant.Constant.PATH_CANCEL;
import static com.recurrence.pagbank.constant.Constant.PATH_SUSPEND;
import static com.recurrence.pagbank.constant.Constant.REFERENCE_ID;
import static com.recurrence.pagbank.constant.Constant.STATUS;
import static com.recurrence.pagbank.constant.Constant.SUBSCRIPTION_ID;

public class PagBankSubscriptionService extends BaseService implements SubscriptionService {

    private static final String SUBSCRIPTIONS = "/subscriptions";
    private static final String CUSTOMERS = "/customers";

    public PagBankSubscriptionService(ProviderContext context) {
        super(context);
    }

    @Override
    public Subscription create(CreateSubscriptionRequest subscription, RequestOptions requestOptions) {
        RequestValidator.validate(subscription);
        SubscriptionValidator.validateCreate(subscription);
        return execute(
                SUBSCRIPTION_CREATE,
                request(POST, SUBSCRIPTIONS, requestOptions)
                        .body(toJson(SUBSCRIPTION_CREATE, PagBankSubscriptionMapper.toRequest(subscription)))
                        .build(),
                PagBankSubscription.class,
                PagBankSubscriptionMapper::toContract
        );
    }

    @Override
    public Subscription get(String subscriptionId) {
        RequestValidator.requireId(subscriptionId, "subscriptionId");
        return execute(
                "subscription.get",
                request(GET, subscriptionPath(subscriptionId), null).build(),
                PagBankSubscription.class,
                PagBankSubscriptionMapper::toContract
        );
    }

    @Override
    public PageResponse<Subscription> list(PageOptions pageOptions, SubscriptionFilter subscriptionFilter) {
        QueryParams query = QueryParams.create().page(pageOptions);
        if (subscriptionFilter != null) {
            query.add(STATUS, PagBankFilterMapper.status(subscriptionFilter.getStatus()))
                    .add("q", subscriptionFilter.getQuery())
                    .add(REFERENCE_ID, subscriptionFilter.getReferenceId());
        }
        return execute(
                SUBSCRIPTION_LIST,
                request(GET, query.appendTo(SUBSCRIPTIONS), null).build(),
                PagbankListSubscription.class,
                PagBankSubscriptionMapper::toPage
        );
    }

    /**
     * No PagBank o cartão pertence ao assinante: a troca de meio de pagamento vai
     * para {@code PUT /customers/{id}/billing_info}, e plano/valor para
     * {@code PUT /subscriptions/{id}}. Ao final devolve a assinatura atualizada.
     */
    @Override
    public Subscription update(String subscriptionId, UpdateSubscriptionRequest subscription, RequestOptions requestOptions) {
        RequestValidator.requireId(subscriptionId, SUBSCRIPTION_ID);
        RequestValidator.validate(subscription);
        SubscriptionValidator.validateSecurityCode(subscription.getPaymentMethod());

        if (subscription.getPaymentMethod() != null) {
            String customerId = get(subscriptionId).getCustomer().id();
            execute(
                    SUBSCRIPTION_UPDATE_BILLING,
                    request(PUT, CUSTOMERS + "/" + segment(customerId) + PATH_BILLING_INFO, requestOptions)
                            .body(toJson(SUBSCRIPTION_UPDATE_BILLING, PagBankSubscriptionMapper.toBillingInfoRequest(subscription.getPaymentMethod())))
                            .build()
            );
        }

        if (subscription.getPlanId() != null || subscription.getAmount() != null) {
            execute(
                    SUBSCRIPTION_UPDATE,
                    request(PUT, subscriptionPath(subscriptionId), requestOptions)
                            .body(toJson(SUBSCRIPTION_UPDATE, PagBankSubscriptionMapper.toRequest(subscription)))
                            .build()
            );
        }

        return get(subscriptionId);
    }

    @Override
    public void suspend(String subscriptionId, RequestOptions requestOptions) {
        changeStatus(SUBSCRIPTION_SUSPEND, subscriptionId, PATH_SUSPEND, requestOptions);
    }

    @Override
    public void cancel(String subscriptionId, RequestOptions requestOptions) {
        changeStatus(SUBSCRIPTION_CANCEL, subscriptionId, PATH_CANCEL, requestOptions);
    }

    @Override
    public void activate(String subscriptionId, RequestOptions requestOptions) {
        changeStatus(SUBSCRIPTION_ACTIVATE, subscriptionId, PATH_ACTIVE, requestOptions);
    }

    private void changeStatus(String operation, String subscriptionId, String action, RequestOptions requestOptions) {
        RequestValidator.requireId(subscriptionId, SUBSCRIPTION_ID);
        execute(operation, request(PUT, subscriptionPath(subscriptionId) + action, requestOptions).build());
    }

    private static String subscriptionPath(String subscriptionId) {
        return SUBSCRIPTIONS + "/" + segment(subscriptionId);
    }
}
