package com.recurrence.contract;

import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.domain.request.CreateSubscriptionRequest;
import com.recurrence.contract.domain.request.UpdateSubscriptionRequest;
import com.recurrence.contract.filters.PageOptions;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.contract.filters.SubscriptionFilter;
import com.recurrence.exception.RecurrenceException;

public interface SubscriptionService {
    default Subscription create(CreateSubscriptionRequest subscription) throws RecurrenceException  {
        return create(
                subscription,
                RequestOptions.empty()
        );
    }
    Subscription create(CreateSubscriptionRequest subscription, RequestOptions requestOptions) throws RecurrenceException;

    Subscription get(String subscriptionId) throws RecurrenceException;

    PageResponse<Subscription> list(PageOptions pageOptions) throws RecurrenceException;
    PageResponse<Subscription> list(PageOptions pageOptions, RequestOptions requestOptions) throws RecurrenceException;
    PageResponse<Subscription> list(PageOptions pageOptions, SubscriptionFilter subscriptionFilter) throws RecurrenceException;
    PageResponse<Subscription> list(PageOptions pageOptions, SubscriptionFilter subscriptionFilter, RequestOptions requestOptions) throws RecurrenceException;

    Subscription update(String subscriptionId, UpdateSubscriptionRequest subscription) throws RecurrenceException;
    Subscription update(String subscriptionId, UpdateSubscriptionRequest subscription, RequestOptions requestOptions) throws RecurrenceException;

    Void suspend(String subscriptionId) throws RecurrenceException;
    Void suspend(String subscriptionId, RequestOptions requestOptions) throws RecurrenceException;

    Void cancel(String subscriptionId) throws RecurrenceException;
    Void cancel(String subscriptionId, RequestOptions requestOptions) throws RecurrenceException;

    Void active(String subscriptionId) throws RecurrenceException;
    Void active(String subscriptionId, RequestOptions requestOptions) throws RecurrenceException;

}
