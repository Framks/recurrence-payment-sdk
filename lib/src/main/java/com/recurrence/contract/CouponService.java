package com.recurrence.contract;

import com.recurrence.contract.domain.Coupon;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.exception.RecurrenceException;

public interface CouponService {
    RequestResponse<Coupon> create(Coupon coupon) throws RecurrenceException;
    RequestResponse<Coupon> create(Coupon coupon, RequestOptions requestOptions) throws RecurrenceException;

    RequestResponse<Coupon> get(String id) throws RecurrenceException;
    RequestResponse<PageResponse<Coupon>> list(Integer offset, Integer limit) throws RecurrenceException;
    // ToDo pode ser pesquisado pelo status também

    RequestResponse<Void> activate(String id) throws RecurrenceException;
    RequestResponse<Void> activate(String id, RequestOptions requestOptions) throws RecurrenceException;

    RequestResponse<Void> inactivate(String id) throws RecurrenceException;
    RequestResponse<Void> inactivate(String id, RequestOptions requestOptions) throws RecurrenceException;

}
