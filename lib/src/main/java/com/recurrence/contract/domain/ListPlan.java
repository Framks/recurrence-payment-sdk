package com.recurrence.contract.domain;

import java.util.List;

public class ListPlan {
    private ResultSet resultSet;
    private List<Plan> plans;

    private ListPlan() {
    }

    public record ResultSet(
            int offset,
            int limit,
            int total
    ){}

    public List<Plan> getPlans() {
        return plans;
    }

    public void setPlans(List<Plan> plans) {
        this.plans = plans;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int offset;
        private int limit;
        private int total;
        private List<Plan> plans;

        public Builder setOffset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder setLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder setTotal(int total) {
            this.total = total;
            return this;
        }

        public Builder setPlans(List<Plan> plans) {
            this.plans = plans;
            return this;
        }

        public ListPlan build() {
            ListPlan listPlan = new ListPlan();
            listPlan.setResultSet(new ResultSet(offset, limit, total));
            listPlan.setPlans(plans);
            return listPlan;
        }
    }
}
