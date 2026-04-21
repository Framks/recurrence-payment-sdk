package com.recurrence.pagbank.domain.payment;

import com.recurrence.pagbank.domain.commons.ResultSet;
import java.util.List;

public class PagBankListRefund {
    private ResultSet resultSet;
    private List<PagBankRefund> refunds;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public List<PagBankRefund> getRefunds() {
        return refunds;
    }

    public void setRefunds(List<PagBankRefund> refunds) {
        this.refunds = refunds;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
