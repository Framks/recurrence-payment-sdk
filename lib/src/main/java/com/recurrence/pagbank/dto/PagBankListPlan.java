package com.recurrence.pagbank.dto;

import com.recurrence.pagbank.domain.plan.PagBankPlan;
import java.util.List;

public class PagBankListPlan {
    private ResultSet ResultSet;
    private List<PagBankPlan> plans;

    public ResultSet getResultSet() {
        return ResultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        ResultSet = resultSet;
    }

    public List<PagBankPlan> getPlans() {
        return plans;
    }

    public void setPlans(List<PagBankPlan> plans) {
        this.plans = plans;
    }

    @Override
    public String toString() {
        return "PagBankListPlan{" +
                "ResultSet=" + ResultSet +
                ", plans=" + plans +
                '}';
    }
}