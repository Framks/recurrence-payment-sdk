package com.recurrence.contract;

import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Payment;
import com.recurrence.contract.filters.PaymentFilter;
import com.recurrence.contract.filters.PageOptions;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.exception.RecurrenceException;

/**
 * Consulta de faturas e pagamentos gerados pelas assinaturas.
 */
public interface InvoiceService {

    Invoice getInvoice(String invoiceId) throws RecurrenceException;

    Payment getPayment(String paymentId) throws RecurrenceException;

    default PageResponse<Payment> listPayments(String invoiceId) throws RecurrenceException {
        return listPayments(invoiceId, null);
    }

    /** Pagamentos (tentativas de cobrança) de uma fatura. */
    PageResponse<Payment> listPayments(String invoiceId, PaymentFilter paymentFilter) throws RecurrenceException;

    default PageResponse<Payment> listPayments(PageOptions pageOptions) throws RecurrenceException {
        return listPayments(pageOptions, null);
    }

    // ToDo avaliar na documentação do Mercado Pago se a listagem geral de pagamentos se mantém no contrato.
    PageResponse<Payment> listPayments(PageOptions pageOptions, PaymentFilter paymentFilter) throws RecurrenceException;

    PageResponse<Invoice> listInvoiceBySubscription(String subscriptionId, PageOptions pageOptions) throws RecurrenceException;
}
