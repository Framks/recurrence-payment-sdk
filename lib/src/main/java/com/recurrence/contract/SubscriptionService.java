package com.recurrence.contract;

import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;

public interface SubscriptionService {
    RequestResponse<Subscription> create(Subscription subscription) throws Exception;
    RequestResponse<Subscription> get(String id) throws Exception;
    RequestResponse<PageResponse<Subscription>> list(int offset, int limit, String query) throws Exception;
    RequestResponse<PageResponse<Invoice>> listInvoiceBySubscription(String subscriptionId, int offset, int limit) throws Exception;
    RequestResponse<Subscription> update(String id, Subscription subscription) throws Exception;
    RequestResponse<Void> suspend(String id) throws Exception;
    RequestResponse<Void> cancel(String id) throws Exception;
    RequestResponse<Void> active(String id) throws Exception;
    RequestResponse<Void> deleteCoupon(String id) throws Exception;
    RequestResponse<Void> retry(String id) throws Exception;
}
