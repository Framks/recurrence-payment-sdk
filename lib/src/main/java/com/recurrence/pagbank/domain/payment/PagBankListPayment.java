package com.recurrence.pagbank.domain.payment;

import com.recurrence.pagbank.domain.commons.ResultSet;
import java.util.List;

public class PagBankListPayment {
    private ResultSet resultSet;
    private List<PagBankPayment> payments;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public List<PagBankPayment> getPayments() {
        return payments;
    }

    public void setPayments(List<PagBankPayment> payments) {
        this.payments = payments;
    }
}
