package com.recurrence.pagbank.domain.subscription;

import com.recurrence.pagbank.domain.commons.ResultSet;
import java.util.List;

public class PagbankListSubscription {
    private ResultSet resultSet;
    private List<PagBankSubscription> subscriptions;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public List<PagBankSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<PagBankSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
