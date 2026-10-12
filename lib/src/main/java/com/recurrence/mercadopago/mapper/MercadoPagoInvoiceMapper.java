package com.recurrence.mercadopago.mapper;

import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Payment;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.commons.CustomerReference;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.mercadopago.domain.payment.MercadoPagoAuthorizedPayment;
import com.recurrence.mercadopago.domain.payment.MercadoPagoAuthorizedPaymentList;
import com.recurrence.mercadopago.domain.payment.MercadoPagoPayment;
import com.recurrence.mercadopago.domain.payment.MercadoPagoPaymentList;

import java.util.List;

/**
 * Fatura do contrato = {@code authorized_payment}; pagamento = {@code /v1/payments}.
 */
public final class MercadoPagoInvoiceMapper {

    private MercadoPagoInvoiceMapper() {}

    /**
     * O Mercado Pago não numera o ciclo ({@code occurrence} fica nulo) nem traz o
     * plano na fatura ({@code plan} fica nulo; use a assinatura para obtê-lo).
     */
    public static Invoice toContract(MercadoPagoAuthorizedPayment invoice) {
        String paymentStatus = invoice.payment() == null ? null : invoice.payment().status();
        return Invoice.builder()
                .id(invoice.id())
                .status(CommonsMapper.toInvoiceStatus(invoice.status(), paymentStatus))
                .amount(CommonsMapper.toContract(invoice.transactionAmount(), invoice.currencyId()))
                .subscriptionId(invoice.preapprovalId())
                .customer(invoice.payerId() == null ? null : new CustomerReference(invoice.payerId(), null, null))
                .createdAt(invoice.dateCreated())
                .updatedAt(invoice.lastModified())
                .build();
    }

    /**
     * O pagamento do Mercado Pago não traz o id da fatura ({@code authorized_payment});
     * o {@code transaction_data.invoice_id} é outro identificador e não serve em
     * {@code getInvoice} (verificado no sandbox). Por isso {@code invoiceId} fica nulo aqui.
     */
    public static Payment toContract(MercadoPagoPayment payment) {
        return toContract(payment, null);
    }

    /** Usado quando a fatura de origem já é conhecida (pagamentos de uma fatura). */
    public static Payment toContract(MercadoPagoPayment payment, String invoiceId) {
        MercadoPagoPayment.Payer payer = payment.payer();
        return Payment.builder()
                .id(payment.id())
                .status(CommonsMapper.toPaymentStatus(payment.status()))
                .invoiceId(invoiceId)
                .amount(CommonsMapper.toContract(payment.transactionAmount(), payment.currencyId()))
                .paymentMethod(toBillingInfo(payment))
                .customer(payer == null ? null : new CustomerReference(payer.id(), null, payer.email()))
                .createdAt(payment.dateCreated())
                .updatedAt(payment.dateLastUpdated())
                .build();
    }

    public static PageResponse<Invoice> toInvoicePage(MercadoPagoAuthorizedPaymentList list) {
        return CommonsMapper.toPage(list.paging(), list.results(), MercadoPagoInvoiceMapper::toContract);
    }

    public static PageResponse<Payment> toPaymentPage(MercadoPagoPaymentList list) {
        return CommonsMapper.toPage(list.paging(), list.results(), MercadoPagoInvoiceMapper::toContract);
    }

    /** Página com os pagamentos de uma fatura (no Mercado Pago, no máximo um). */
    public static PageResponse<Payment> paymentPageOf(List<Payment> payments) {
        return new PageResponse<>(new PageResponse.PageInfo(0, payments.size(), payments.size()), payments);
    }

    private static BillingInfo toBillingInfo(MercadoPagoPayment payment) {
        if (payment.paymentTypeId() == null) return null;
        return BillingInfo.builder()
                .type(CommonsMapper.toPaymentMethodType(payment.paymentTypeId()))
                .card(Card.builder()
                        .brand(payment.paymentMethodId())
                        .lastDigits(payment.card() == null ? null : payment.card().lastFourDigits())
                        .build())
                .build();
    }
}
