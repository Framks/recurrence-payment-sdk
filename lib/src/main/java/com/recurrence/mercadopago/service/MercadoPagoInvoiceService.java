package com.recurrence.mercadopago.service;

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
import com.recurrence.mercadopago.domain.payment.MercadoPagoAuthorizedPayment;
import com.recurrence.mercadopago.domain.payment.MercadoPagoAuthorizedPaymentList;
import com.recurrence.mercadopago.domain.payment.MercadoPagoPayment;
import com.recurrence.mercadopago.domain.payment.MercadoPagoPaymentList;
import com.recurrence.mercadopago.mapper.MercadoPagoFilterMapper;
import com.recurrence.mercadopago.mapper.MercadoPagoInvoiceMapper;
import com.recurrence.mercadopago.validator.PaymentFilterValidator;

import java.util.List;

import static com.recurrence.infrastructure.MethodRequest.GET;
import static com.recurrence.mercadopago.constant.Constant.BEGIN_DATE;
import static com.recurrence.mercadopago.constant.Constant.CRITERIA;
import static com.recurrence.mercadopago.constant.Constant.DATE_CREATED;
import static com.recurrence.mercadopago.constant.Constant.DESC;
import static com.recurrence.mercadopago.constant.Constant.END_DATE;
import static com.recurrence.mercadopago.constant.Constant.INVOICES;
import static com.recurrence.mercadopago.constant.Constant.INVOICE_ID;
import static com.recurrence.mercadopago.constant.Constant.Operation;
import static com.recurrence.mercadopago.constant.Constant.PATH_SEARCH;
import static com.recurrence.mercadopago.constant.Constant.PAYMENTS;
import static com.recurrence.mercadopago.constant.Constant.PAYMENT_ID;
import static com.recurrence.mercadopago.constant.Constant.PREAPPROVAL_ID;
import static com.recurrence.mercadopago.constant.Constant.RANGE;
import static com.recurrence.mercadopago.constant.Constant.SORT;
import static com.recurrence.mercadopago.constant.Constant.STATUS;
import static com.recurrence.mercadopago.constant.Constant.SUBSCRIPTION_ID;

/**
 * Faturas ({@code /authorized_payments}) e pagamentos ({@code /v1/payments}) do Mercado Pago.
 */
public class MercadoPagoInvoiceService extends BaseService implements InvoiceService {

    public MercadoPagoInvoiceService(ProviderContext context) {
        super(context);
    }

    @Override
    public Invoice getInvoice(String invoiceId) {
        RequestValidator.requireId(invoiceId, INVOICE_ID);
        return execute(
                Operation.INVOICE_GET,
                request(GET, INVOICES + "/" + segment(invoiceId), null).build(),
                MercadoPagoAuthorizedPayment.class,
                MercadoPagoInvoiceMapper::toContract
        );
    }

    @Override
    public Payment getPayment(String paymentId) {
        RequestValidator.requireId(paymentId, PAYMENT_ID);
        return execute(
                Operation.PAYMENT_GET,
                request(GET, PAYMENTS + "/" + segment(paymentId), null).build(),
                MercadoPagoPayment.class,
                MercadoPagoInvoiceMapper::toContract
        );
    }

    /**
     * No Mercado Pago a fatura referencia no máximo um pagamento
     * ({@code authorized_payment.payment.id}): busca a fatura e, se houver, o pagamento.
     * O filtro é aplicado sobre esse resultado.
     */
    @Override
    public PageResponse<Payment> listPayments(String invoiceId, PaymentFilter paymentFilter) {
        RequestValidator.requireId(invoiceId, INVOICE_ID);
        if (paymentFilter != null) {
            PaymentFilterValidator.validate(paymentFilter);
            MercadoPagoFilterMapper.paymentStatus(paymentFilter.getStatus());
        }
        MercadoPagoAuthorizedPayment invoice = execute(
                Operation.INVOICE_LIST_PAYMENT,
                request(GET, INVOICES + "/" + segment(invoiceId), null).build(),
                MercadoPagoAuthorizedPayment.class,
                authorizedPayment -> authorizedPayment
        );
        if (invoice.payment() == null || invoice.payment().id() == null) {
            return MercadoPagoInvoiceMapper.paymentPageOf(List.of());
        }
        Payment payment = execute(
                Operation.INVOICE_LIST_PAYMENT,
                request(GET, PAYMENTS + "/" + segment(invoice.payment().id()), null).build(),
                MercadoPagoPayment.class,
                mercadoPagoPayment -> MercadoPagoInvoiceMapper.toContract(mercadoPagoPayment, invoiceId)
        );
        return MercadoPagoInvoiceMapper.paymentPageOf(matches(payment, paymentFilter) ? List.of(payment) : List.of());
    }

    @Override
    public PageResponse<Payment> listPayments(PageOptions pageOptions, PaymentFilter paymentFilter) {
        QueryParams query = QueryParams.create()
                .page(pageOptions)
                .add(SORT, DATE_CREATED)
                .add(CRITERIA, DESC);
        if (paymentFilter != null) {
            PaymentFilterValidator.validate(paymentFilter);
            query.add(STATUS, MercadoPagoFilterMapper.paymentStatus(paymentFilter.getStatus()));
            if (paymentFilter.getCreatedFrom() != null) {
                query.add(RANGE, DATE_CREATED)
                        .add(BEGIN_DATE, MercadoPagoFilterMapper.dateTime(paymentFilter.getCreatedFrom()))
                        .add(END_DATE, MercadoPagoFilterMapper.dateTime(paymentFilter.getCreatedUntil()));
            }
        }
        return execute(
                Operation.PAYMENT_LIST,
                request(GET, query.appendTo(PAYMENTS + PATH_SEARCH), null).build(),
                MercadoPagoPaymentList.class,
                MercadoPagoInvoiceMapper::toPaymentPage
        );
    }

    @Override
    public PageResponse<Invoice> listInvoiceBySubscription(String subscriptionId, PageOptions pageOptions) {
        RequestValidator.requireId(subscriptionId, SUBSCRIPTION_ID);
        String path = QueryParams.create()
                .add(PREAPPROVAL_ID, subscriptionId)
                .page(pageOptions)
                .appendTo(INVOICES + PATH_SEARCH);
        return execute(
                Operation.SUBSCRIPTION_INVOICE_LIST,
                request(GET, path, null).build(),
                MercadoPagoAuthorizedPaymentList.class,
                MercadoPagoInvoiceMapper::toInvoicePage
        );
    }

    private static boolean matches(Payment payment, PaymentFilter filter) {
        if (filter == null) return true;
        if (filter.getStatus() != null && filter.getStatus() != payment.getStatus()) return false;
        if (filter.getCreatedFrom() != null && payment.getCreatedAt() != null) {
            return !payment.getCreatedAt().isBefore(filter.getCreatedFrom())
                    && !payment.getCreatedAt().isAfter(filter.getCreatedUntil());
        }
        return true;
    }
}
