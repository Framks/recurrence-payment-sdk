package com.recurrence.pagbank.service;

import com.recurrence.contract.InvoiceService;
import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Payment;
import com.recurrence.contract.filters.PageOptions;
import com.recurrence.contract.filters.PaymentFilter;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.core.BaseService;
import com.recurrence.core.ProviderContext;
import com.recurrence.core.QueryParams;
import com.recurrence.core.RequestValidator;
import com.recurrence.pagbank.constant.Constant;
import com.recurrence.pagbank.domain.payment.PagBankInvoice;
import com.recurrence.pagbank.domain.payment.PagBankListInvoice;
import com.recurrence.pagbank.domain.payment.PagBankListPayment;
import com.recurrence.pagbank.domain.payment.PagBankPayment;
import com.recurrence.pagbank.mapper.PagBankFilterMapper;
import com.recurrence.pagbank.mapper.PagBankInvoiceMapper;
import com.recurrence.pagbank.validator.PaymentFilterValidator;

import static com.recurrence.infrastructure.MethodRequest.GET;
import static com.recurrence.pagbank.constant.Constant.CREATE_AT_END;
import static com.recurrence.pagbank.constant.Constant.CREATE_AT_START;
import static com.recurrence.pagbank.constant.Constant.INVOICE_ID;
import static com.recurrence.pagbank.constant.Constant.Operation.INVOICE_GET;
import static com.recurrence.pagbank.constant.Constant.Operation.INVOICE_LIST_PAYMENT;
import static com.recurrence.pagbank.constant.Constant.Operation.PAYMENT_GET;
import static com.recurrence.pagbank.constant.Constant.Operation.PAYMENT_LIST;
import static com.recurrence.pagbank.constant.Constant.Operation.SUBSCRIPTION_INVOICE_LIST;
import static com.recurrence.pagbank.constant.Constant.PAYMENT_ID;
import static com.recurrence.pagbank.constant.Constant.SUBSCRIPTION_ID;

public class PagBankInvoiceService extends BaseService implements InvoiceService {

    private static final String INVOICES = "/invoices";
    private static final String PAYMENTS = "/payments";
    private static final String SUBSCRIPTIONS = "/subscriptions";

    public PagBankInvoiceService(ProviderContext context) {
        super(context);
    }

    @Override
    public Invoice getInvoice(String invoiceId) {
        RequestValidator.requireId(invoiceId, INVOICE_ID);
        return execute(
                INVOICE_GET,
                request(GET, INVOICES + "/" + segment(invoiceId), null).build(),
                PagBankInvoice.class,
                PagBankInvoiceMapper::toContract
        );
    }

    @Override
    public Payment getPayment(String paymentId) {
        RequestValidator.requireId(paymentId, PAYMENT_ID);
        return execute(
                PAYMENT_GET,
                request(GET, PAYMENTS + "/" + segment(paymentId), null).build(),
                PagBankPayment.class,
                PagBankInvoiceMapper::toContract
        );
    }

    @Override
    public PageResponse<Payment> listPayments(String invoiceId, PaymentFilter paymentFilter) {
        RequestValidator.requireId(invoiceId, INVOICE_ID);
        String path = withFilter(QueryParams.create(), paymentFilter)
                .appendTo(INVOICES + "/" + segment(invoiceId) + PAYMENTS);
        return execute(
                INVOICE_LIST_PAYMENT,
                request(GET, path, null).build(),
                PagBankListPayment.class,
                PagBankInvoiceMapper::toPaymentPage
        );
    }

    @Override
    public PageResponse<Payment> listPayments(PageOptions pageOptions, PaymentFilter paymentFilter) {
        String path = withFilter(QueryParams.create().page(pageOptions), paymentFilter).appendTo(PAYMENTS);
        return execute(
                PAYMENT_LIST,
                request(GET, path, null).build(),
                PagBankListPayment.class,
                PagBankInvoiceMapper::toPaymentPage
        );
    }

    @Override
    public PageResponse<Invoice> listInvoiceBySubscription(String subscriptionId, PageOptions pageOptions) {
        RequestValidator.requireId(subscriptionId, SUBSCRIPTION_ID);
        String path = QueryParams.create()
                .page(pageOptions)
                .appendTo(SUBSCRIPTIONS + "/" + segment(subscriptionId) + INVOICES);
        return execute(
                SUBSCRIPTION_INVOICE_LIST,
                request(GET, path, null).build(),
                PagBankListInvoice.class,
                PagBankInvoiceMapper::toInvoicePage
        );
    }

    private static QueryParams withFilter(QueryParams query, PaymentFilter paymentFilter) {
        if (paymentFilter == null) {
            return query;
        }
        PaymentFilterValidator.validate(paymentFilter);
        return query.add(Constant.STATUS, PagBankFilterMapper.paymentStatus(paymentFilter.getStatus()))
                .add(CREATE_AT_START, PagBankFilterMapper.date(paymentFilter.getCreatedFrom()))
                .add(CREATE_AT_END, PagBankFilterMapper.date(paymentFilter.getCreatedUntil()));
    }
}
