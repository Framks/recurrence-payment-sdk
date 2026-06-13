package com.recurrence.contract;

import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.contract.filters.SubscriptionFilter;
import com.recurrence.exception.RecurrenceException;

public interface SubscriptionService {
    RequestResponse<Subscription> create(Subscription subscription) throws RecurrenceException;
    RequestResponse<Subscription> create(Subscription subscription, RequestOptions requestOptions) throws RecurrenceException;
    RequestResponse<Subscription> get(String id) throws RecurrenceException;
    RequestResponse<PageResponse<Subscription>> list() throws RecurrenceException;
    RequestResponse<PageResponse<Subscription>> list(SubscriptionFilter subscriptionFilter) throws RecurrenceException;
    RequestResponse<PageResponse<Subscription>> list(SubscriptionFilter subscriptionFilter, RequestOptions requestOptions);

    RequestResponse<PageResponse<Invoice>> listInvoiceBySubscription(String subscriptionId, int offset, int limit) throws RecurrenceException;
    RequestResponse<Subscription> update(String id, Subscription subscription) throws RecurrenceException;
    RequestResponse<Void> suspend(String id) throws RecurrenceException;
    RequestResponse<Void> cancel(String id) throws RecurrenceException;
    RequestResponse<Void> active(String id) throws RecurrenceException;
    RequestResponse<Void> deleteCoupon(String id) throws RecurrenceException;
    RequestResponse<Void> retry(String id) throws RecurrenceException;
}
