package com.recurrence.contract;

import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.Customer;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.exception.RecurrenceException;

public interface CustomerService {
        RequestResponse<Customer> create(Customer customer) throws RecurrenceException;
        RequestResponse<Customer> create(Customer customer, RequestOptions requestOptions) throws RecurrenceException;

        RequestResponse<Customer> get(String id) throws RecurrenceException;

        // todo fazer o filter do customer
        RequestResponse<PageResponse<Customer>> list(Integer offset, Integer limit, String query) throws RecurrenceException;

        RequestResponse<Customer> update(String id, Customer customer) throws RecurrenceException;
        RequestResponse<Customer> update(String id, Customer customer, RequestOptions requestOptions) throws RecurrenceException;

        RequestResponse<Customer> updateBillingInfo(String id, BillingInfo billingInfo) throws RecurrenceException;
        RequestResponse<Customer> updateBillingInfo(String id, BillingInfo billingInfo, RequestOptions requestOptions) throws RecurrenceException;
}
