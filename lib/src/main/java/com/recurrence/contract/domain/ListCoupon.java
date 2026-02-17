package com.recurrence.contract.domain;

import java.util.List;

public class ListCoupon {

    private ResultSet resultSet;
    private List<Coupon> coupons;

    public record ResultSet(
            Long offset,
            Long limit,
            Long total
    ){}

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
