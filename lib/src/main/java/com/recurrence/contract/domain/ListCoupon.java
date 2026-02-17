package com.recurrence.contract.domain;

import java.util.List;

public class ListCoupon {

    private ResultSet resultSet;
    private List<Coupon> coupons;

    public record ResultSet(
            int offset,
            int limit,
            int total
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int offset;
        private int limit;
        private int total;
        private List<Coupon> coupons;

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

        public Builder setCoupons(List<Coupon> coupons) {
            this.coupons = coupons;
            return this;
        }

        public ListCoupon build() {
            ListCoupon listCoupon = new ListCoupon();
            listCoupon.setResultSet(new ResultSet(offset, limit, total));
            listCoupon.setCoupons(coupons);
            return listCoupon;
        }
    }
}
