package com.recurrence.pagbank.domain.coupon;

import com.recurrence.pagbank.domain.commons.ResultSet;
import java.util.List;

public class PagBankListCoupon {
    ResultSet ResultSet;
    private List<PagBankCoupon> coupons;

    public ResultSet getResultSet() {
        return ResultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        ResultSet = resultSet;
    }

    public List<PagBankCoupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<PagBankCoupon> coupons) {
        this.coupons = coupons;
    }
}
