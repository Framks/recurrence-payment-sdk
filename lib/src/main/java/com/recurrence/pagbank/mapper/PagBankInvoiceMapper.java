package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.Customer;
import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Payment;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.Refund;
import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.pagbank.domain.commons.PagBankAmount;
import com.recurrence.pagbank.domain.payment.PagBankInvoice;
import com.recurrence.pagbank.domain.payment.PagBankListInvoice;
import com.recurrence.pagbank.domain.payment.PagBankListPayment;
import com.recurrence.pagbank.domain.payment.PagBankListRefund;
import com.recurrence.pagbank.domain.payment.PagBankPayment;
import com.recurrence.pagbank.domain.payment.PagBankRefund;
import java.util.stream.Collectors;

public class PagBankInvoiceMapper {

    public static Invoice toContract(PagBankInvoice pagBankInvoice) {
        if (pagBankInvoice == null) return null;

        return Invoice.builder()
                .id(pagBankInvoice.getId())
                .status(pagBankInvoice.getStatus())
                .occurrence(pagBankInvoice.getOccurrence())
                .amount(CommonsMapper.toContractAmount(pagBankInvoice.getAmount()))
                .subscription(Subscription.builder().id(pagBankInvoice.getSubscription().getId()).build())
                .customer(Customer.builder()
                    .id(pagBankInvoice.getCustomer().getId())
                    .email(pagBankInvoice.getCustomer().getEmail())
                    .name(pagBankInvoice.getCustomer().getName())
                    .build()
                )
                .plan(Plan.builder()
                    .id(pagBankInvoice.getPlan().getId())
                    .name(pagBankInvoice.getPlan().getName())
                    .build()
                )
                .createdAt(pagBankInvoice.getCreatedAt())
                .updatedAt(pagBankInvoice.getUpdatedAt())
                .items(pagBankInvoice.getItems() != null ? pagBankInvoice.getItems().stream()
                        .map(item -> new Invoice.Item(item.type(), CommonsMapper.toContractAmount(item.amount())))
                        .collect(Collectors.toList()) : null)
                .build();
    }

    public static Payment toContract(PagBankPayment pagBankPayment) {
        if (pagBankPayment == null) return null;

        return Payment.builder()
            .id(pagBankPayment.getId())
            .status(pagBankPayment.getStatus())
            .createdAt(pagBankPayment.getCreatedAt())
            .updatedAt(pagBankPayment.getUpdatedAt())
            .invoiceId(pagBankPayment.getInvoice() != null ? pagBankPayment.getInvoice().getId() : null)
            .amount(pagBankPayment.getInvoice() != null ? CommonsMapper.toContractAmount(pagBankPayment.getInvoice().getAmount()) : null)
            .paymentMethod(CommonsMapper.toContractBillingInfo(pagBankPayment.getPaymentMethod()))
            .customer(Customer.builder()
                .id(pagBankPayment.getCustomer().getId())
                .email(pagBankPayment.getCustomer().getEmail())
                .name(pagBankPayment.getCustomer().getName())
                .build()
            )
            .build();
    }

    public static PagBankRefund toRefundPayload(Amount amount){
        if (amount.value() <= 0) {
            throw new IllegalArgumentException("Amount value must be greater than zero.");
        }
        if (amount.currency() == null || amount.currency().isEmpty()) {
            throw new IllegalArgumentException("Currency must be provided.");
        }

        var refund = new PagBankRefund();
        refund.setAmount(new PagBankAmount(
            amount.value(),
            amount.currency()
        ));
        return refund;
    }

    public static Refund toContract(PagBankRefund pagBankRefund) {
        if (pagBankRefund == null) return null;

        return Refund.builder()
                .id(pagBankRefund.getId())
                .status(pagBankRefund.getStatus())
                .type(pagBankRefund.getType())
                .createdAt(pagBankRefund.getCreatedAt())
                .paymentId(Payment.builder()
                    .id(pagBankRefund.getPayment().id())
                    .amount(CommonsMapper.toContractAmount(pagBankRefund.getPayment().amount()))
                    .build())
                .amount(CommonsMapper.toContractAmount(pagBankRefund.getAmount()))
                .build();
    }

    public static PageResponse<Invoice> toInvoicePageResponse(PagBankListInvoice listInvoice) {
        return new PageResponse<>(
                new PageResponse.PageInfo(
                        listInvoice.getResultSet().offset(),
                        listInvoice.getResultSet().limit(),
                        listInvoice.getResultSet().total()
                ),
                listInvoice.getInvoices().stream()
                        .map(PagBankInvoiceMapper::toContract)
                        .collect(Collectors.toList())
        );
    }

    public static PageResponse<Payment> toPageResponse(PagBankListPayment listPayment) {
        return new PageResponse<>(
                new PageResponse.PageInfo(
                        listPayment.getResultSet().offset(),
                        listPayment.getResultSet().limit(),
                        listPayment.getResultSet().total()
                ),
                listPayment.getPayments().stream()
                        .map(PagBankInvoiceMapper::toContract)
                        .collect(Collectors.toList())
        );
    }

    public static PageResponse<Refund> toPageResponse(PagBankListRefund listRefund) {
        return new PageResponse<>(
                new PageResponse.PageInfo(
                        listRefund.getResultSet().offset(),
                        listRefund.getResultSet().limit(),
                        listRefund.getResultSet().total()
                ),
                listRefund.getRefunds().stream()
                        .map(PagBankInvoiceMapper::toContract)
                        .collect(Collectors.toList())
        );
    }
}
