package com.recurrence.contract;

import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.Customer;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;

public interface CustomerService {
        RequestResponse<Customer> create(Customer customer);
        RequestResponse<Customer> get(String id);
        RequestResponse<PageResponse<Customer>> list(Integer offset, Integer limit, String query);
        RequestResponse<Customer> update(String id, Customer customer);
        RequestResponse<Customer> updateBillingInfo(String id, BillingInfo billingInfo);
}
