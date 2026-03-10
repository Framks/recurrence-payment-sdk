package com.recurrence.contract;

import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.Customer;
import com.recurrence.contract.domain.infra.PageResponse;
import com.recurrence.contract.domain.infra.RequestResponse;

public interface CustomerService {
        RequestResponse<Customer> create(Customer subscriber);
        RequestResponse<Customer> get(String id);
        RequestResponse<PageResponse<Customer>> list(Long offset, Long limit);
        RequestResponse<Customer> update(String id, Customer subscriber);
        RequestResponse<Customer> updateBillingInfo(String id, BillingInfo billingInfo);
}
