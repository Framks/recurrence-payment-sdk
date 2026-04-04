package com.recurrence.pagbank.domain.plan;

import com.recurrence.pagbank.domain.commons.ResultSet;
import java.util.List;

public class PagBankListPlan {
    private ResultSet resultSet;
    private List<PagBankPlan> plans;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public List<PagBankPlan> getPlans() {
        return plans;
    }

    public void setPlans(List<PagBankPlan> plans) {
        this.plans = plans;
    }
}