package com.recurrence.pagbank.domain.commons;

import java.util.List;

public class PagBankList<T> {
    private ResultSet resultSet;
    private List<T> list;

    public PagBankList( List<T> list, ResultSet resultSet){
        this.list = list;
        this.resultSet = resultSet;
    }

    public List<T> getList() {
        return list;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
