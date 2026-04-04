package com.recurrence.contract;

import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;

public interface SubscriptionService {
    RequestResponse<Subscription> create(Subscription subscription);
    RequestResponse<Subscription> get(String id);
    RequestResponse<PageResponse<Subscription>> list(int offset, int limit, String query);
    RequestResponse<PageResponse<Void>> listInvoiceBySubscription(String subscriptionId, int offset, int limit);
    RequestResponse<Subscription> update(String id, Subscription subscription);
    RequestResponse<Void> suspend(String id);
    RequestResponse<Void> cancel(String id);
    RequestResponse<Void> active(String id);
    RequestResponse<Void> deleteCoupon(String id);
}
