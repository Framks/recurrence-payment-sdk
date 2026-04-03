package com.recurrence.contract;

import com.recurrence.contract.domain.Coupon;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;

public interface CouponService {
    RequestResponse<Coupon> create(Coupon coupon);
    RequestResponse<Coupon> get(String id);
    RequestResponse<PageResponse<Coupon>> list(int offset, int limit);
    RequestResponse<Void> activate(String id);
    RequestResponse<Void> inactivate(String id);
}