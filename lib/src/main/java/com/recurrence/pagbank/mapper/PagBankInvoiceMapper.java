package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Payment;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.pagbank.domain.payment.PagBankInvoice;
import com.recurrence.pagbank.domain.payment.PagBankListInvoice;
import com.recurrence.pagbank.domain.payment.PagBankListPayment;
import com.recurrence.pagbank.domain.payment.PagBankPayment;

public final class PagBankInvoiceMapper {

    private PagBankInvoiceMapper() {}

    public static Invoice toContract(PagBankInvoice invoice) {
        return Invoice.builder()
                .id(invoice.id())
                .status(CommonsMapper.toInvoiceStatus(invoice.status()))
                .occurrence(invoice.occurrence())
                .amount(CommonsMapper.toContract(invoice.amount()))
                .subscriptionId(invoice.subscription() == null ? null : invoice.subscription().id())
                .plan(CommonsMapper.toPlanReference(invoice.plan()))
                .customer(CommonsMapper.toCustomerReference(invoice.customer()))
                .createdAt(invoice.createdAt())
                .updatedAt(invoice.updatedAt())
                .build();
    }

    /** O PagBank não traz valor no pagamento; usa o valor da fatura cobrada. */
    public static Payment toContract(PagBankPayment payment) {
        PagBankInvoice invoice = payment.invoice();
        return Payment.builder()
                .id(payment.id())
                .status(CommonsMapper.toPaymentStatus(payment.status()))
                .invoiceId(invoice == null ? null : invoice.id())
                .amount(invoice == null ? null : CommonsMapper.toContract(invoice.amount()))
                .paymentMethod(CommonsMapper.toContract(payment.paymentMethod()))
                .customer(CommonsMapper.toCustomerReference(payment.customer()))
                .createdAt(payment.createdAt())
                .updatedAt(payment.updatedAt())
                .build();
    }

    public static PageResponse<Invoice> toInvoicePage(PagBankListInvoice list) {
        return CommonsMapper.toPage(list.resultSet(), list.invoices(), PagBankInvoiceMapper::toContract);
    }

    public static PageResponse<Payment> toPaymentPage(PagBankListPayment list) {
        return CommonsMapper.toPage(list.resultSet(), list.payments(), PagBankInvoiceMapper::toContract);
    }
}
