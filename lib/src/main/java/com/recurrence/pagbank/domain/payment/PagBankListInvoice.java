package com.recurrence.pagbank.domain.payment;

import com.recurrence.pagbank.domain.commons.ResultSet;

import java.util.List;

public record PagBankListInvoice(
        ResultSet resultSet,
        List<PagBankInvoice> invoices
) {
}
