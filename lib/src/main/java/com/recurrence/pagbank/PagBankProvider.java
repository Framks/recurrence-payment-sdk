package com.recurrence.pagbank;

import com.recurrence.contract.CouponService;
import com.recurrence.contract.PlanService;
import com.recurrence.contract.RecurrenceProvider;
import com.recurrence.pagbank.coupon.PagBankCouponService;
import com.recurrence.pagbank.plan.PagBankPlanService;

public class PagBankProvider implements RecurrenceProvider {

    private final PagBankPlanService planService;
    private final PagBankCouponService couponService;

    public PagBankProvider(
            final PagBankEnvironment environment,
            final String token
    ) {
        this.planService = new PagBankPlanService(
                environment,
                token
        );
        this.couponService = new PagBankCouponService(
                environment,
                token
        );
    }

    @Override
    public PlanService planService() {
        return planService;
    }

    @Override
    public CouponService couponService() {
        return couponService;
    }
}
