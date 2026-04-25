package com.recurrence.contract;

import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Payment;
import com.recurrence.contract.domain.Refund;
import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;

public interface InvoiceService {
    RequestResponse<Invoice> getInvoice(String id) throws Exception;
    RequestResponse<Payment> getPayment(String id) throws Exception;
    RequestResponse<PageResponse<Payment>> listPayments(String id) throws Exception;
    RequestResponse<PageResponse<Payment>> listPayments( Long offset, Long limit) throws Exception;
    RequestResponse<Refund> refund(String paymentId, Amount amount) throws Exception;
    RequestResponse<Refund> getRefund(String id) throws Exception;
    RequestResponse<PageResponse<Refund>> listRefunds(Long offset, Long limit) throws Exception;
}
