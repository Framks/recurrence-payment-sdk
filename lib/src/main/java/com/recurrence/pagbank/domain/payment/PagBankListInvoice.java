package com.recurrence.pagbank.domain.payment;

import com.recurrence.pagbank.domain.commons.ResultSet;
import java.util.List;

public class PagBankListInvoice {
    private ResultSet resultSet;
    private List<PagBankInvoice> invoices;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public List<PagBankInvoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<PagBankInvoice> invoices) {
        this.invoices = invoices;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
