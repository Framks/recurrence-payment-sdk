package com.recurrence.contract.domain;

import java.util.List;

public class ListPlan {
    private ResultSet resultSet;
    private List<Plan> plans;

    private ListPlan(Builder builder) {
        this.plans = builder.plans;
        this.resultSet = new ResultSet(
                builder.offset,
                builder.limit,
                builder.total
        );
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

        public Builder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder total(int total) {
            this.total = total;
            return this;
        }

        public Builder plans(List<Plan> plans) {
            this.plans = plans;
            return this;
        }

        public ListPlan build() {
            return new ListPlan(this);
        }
    }
}
