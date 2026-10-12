package com.recurrence.pagbank.domain.payment;

import com.recurrence.pagbank.domain.commons.ResultSet;

import java.util.List;

public record PagBankListPayment(
        ResultSet resultSet,
        List<PagBankPayment> payments
) {
}
