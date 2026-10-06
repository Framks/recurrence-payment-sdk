package com.recurrence.pagbank.domain.subscription;

import com.recurrence.pagbank.domain.commons.ResultSet;

import java.util.List;

public record PagbankListSubscription(
        ResultSet resultSet,
        List<PagBankSubscription> subscriptions
) {
}
