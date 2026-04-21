package com.recurrence.pagbank.domain.commons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagBankReferencesRetry {
    private Integer firstTry;
    private Integer secondTry;
    private Integer thirdTry;
    @JsonProperty("finally")
    private String finallyStatus;

    public PagBankReferencesRetry() {}

    private PagBankReferencesRetry(Builder builder) {
        this.firstTry = builder.firstTry;
        this.secondTry = builder.secondTry;
        this.thirdTry = builder.thirdTry;
        this.finallyStatus = builder.finallyStatus;
    }

    public Integer getFirstTry() {
        return firstTry;
    }
    public Integer getSecondTry() {
        return secondTry;
    }
    public Integer getThirdTry() {
        return thirdTry;
    }
    public String getFinallyStatus() {
        return finallyStatus;
    }

    public void setFirstTry(Integer firstTry) {
        this.firstTry = firstTry;
    }
    public void setSecondTry(Integer secondTry) {
        this.secondTry = secondTry;
    }
    public void setThirdTry(Integer thirdTry) {
        this.thirdTry = thirdTry;
    }
    public void setFinallyStatus(String finallyStatus) {
        this.finallyStatus = finallyStatus;
    }

    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private Integer firstTry;
        private Integer secondTry;
        private Integer thirdTry;
        private String finallyStatus;

        public Builder firstTry(Integer firstTry) {
            this.firstTry = firstTry;
            return this;
        }

        public Builder secondTry(Integer secondTry) {
            this.secondTry = secondTry;
            return this;
        }

        public Builder thirdTry(Integer thirdTry) {
            this.thirdTry = thirdTry;
            return this;
        }

        public Builder finallyStatus(String finallyStatus) {
            this.finallyStatus = finallyStatus;
            return this;
        }

        public PagBankReferencesRetry build() {
            return new PagBankReferencesRetry(this);
        }
    }
}