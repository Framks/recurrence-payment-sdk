package com.recurrence.pagbank.domain.customer;

import com.recurrence.pagbank.domain.commons.ResultSet;
import java.util.List;

public class PagBankListCustomer {
    private ResultSet resultSet;
    private List<PagBankCustomer> customers;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public List<PagBankCustomer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<PagBankCustomer> customers) {
        this.customers = customers;
    }

    @Override
    public String toString(){
        return "PagBankListCustomer{" +
            resultSet +
            ", Customers=" + customers +
            '}';
    }
}
