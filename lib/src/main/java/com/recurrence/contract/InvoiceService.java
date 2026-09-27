package com.recurrence.contract;

import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Payment;
import com.recurrence.contract.filters.PaymentFilter;
import com.recurrence.contract.filters.PageOptions;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.exception.RecurrenceException;

public interface InvoiceService {
    Invoice getInvoice(String invoiceId) throws RecurrenceException;
    Payment getPayment(String paymentId) throws RecurrenceException;

    PageResponse<Payment> listPayments(String invoiceId) throws RecurrenceException;
    PageResponse<Payment> listPayments(String invoiceId, PaymentFilter paymentFilter) throws RecurrenceException;
    PageResponse<Payment> listPayments(PageOptions pageOptions) throws RecurrenceException;
    PageResponse<Payment> listPayments(PageOptions pageOptions, PaymentFilter paymentFilter) throws RecurrenceException;

    PageResponse<Invoice> listInvoiceBySubscription(String subscriptionId, PageOptions pageOptions) throws RecurrenceException;
}
