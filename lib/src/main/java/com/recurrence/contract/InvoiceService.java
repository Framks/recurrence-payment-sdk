package com.recurrence.contract;

import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Payment;
import com.recurrence.contract.domain.Refund;
import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;

public interface InvoiceService {
    RequestResponse<Invoice> getInvoice(String id);
    RequestResponse<Payment> getPayment(String id);
    RequestResponse<PageResponse<Payment>> listPayments(String id);
    RequestResponse<PageResponse<Payment>> listPayments( Long offset, Long limit);
    RequestResponse<Refund> refund(String paymentId, Amount amount);
    RequestResponse<Refund> getRefund(String id);
    RequestResponse<PageResponse<Refund>> listRefunds(Long offset, Long limit);
}
