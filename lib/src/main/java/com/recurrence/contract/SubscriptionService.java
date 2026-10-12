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
 * Operações de assinatura comuns aos provedores.
 *
 * <p>Cada operação tem um único método abstrato; as variações sem
 * {@link RequestOptions} ou sem filtro são {@code default}.</p>
 */
public interface SubscriptionService {

    default Subscription create(CreateSubscriptionRequest subscription) throws RecurrenceException {
        return create(subscription, RequestOptions.empty());
    }

    Subscription create(CreateSubscriptionRequest subscription, RequestOptions requestOptions) throws RecurrenceException;

    Subscription get(String subscriptionId) throws RecurrenceException;

    default PageResponse<Subscription> list(PageOptions pageOptions) throws RecurrenceException {
        return list(pageOptions, null);
    }

    /**
     * @param pageOptions        paginação; {@code null} usa o padrão do provedor
     * @param subscriptionFilter filtros; {@code null} não filtra
     */
    PageResponse<Subscription> list(PageOptions pageOptions, SubscriptionFilter subscriptionFilter) throws RecurrenceException;

    default Subscription update(String subscriptionId, UpdateSubscriptionRequest subscription) throws RecurrenceException {
        return update(subscriptionId, subscription, RequestOptions.empty());
    }

    Subscription update(String subscriptionId, UpdateSubscriptionRequest subscription, RequestOptions requestOptions) throws RecurrenceException;

    default void suspend(String subscriptionId) throws RecurrenceException {
        suspend(subscriptionId, RequestOptions.empty());
    }

    void suspend(String subscriptionId, RequestOptions requestOptions) throws RecurrenceException;

    default void cancel(String subscriptionId) throws RecurrenceException {
        cancel(subscriptionId, RequestOptions.empty());
    }

    void cancel(String subscriptionId, RequestOptions requestOptions) throws RecurrenceException;

    default void activate(String subscriptionId) throws RecurrenceException {
        activate(subscriptionId, RequestOptions.empty());
    }

    void activate(String subscriptionId, RequestOptions requestOptions) throws RecurrenceException;
}
