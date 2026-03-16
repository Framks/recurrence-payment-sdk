package com.recurrence.pagbank;

import com.recurrence.contract.CouponService;
import com.recurrence.contract.CustomerService;
import com.recurrence.contract.InvoiceService;
import com.recurrence.contract.PlanService;
import com.recurrence.contract.RecurrenceProvider;
import com.recurrence.contract.SubscriptionService;
import com.recurrence.pagbank.service.PagBankCouponService;
import com.recurrence.pagbank.service.PagBankPlanService;
import com.recurrence.pagbank.service.PagbankCustomerService;

public class PagBankProvider implements RecurrenceProvider {

    private final PagBankPlanService planService;
    private final PagBankCouponService couponService;
    private final PagbankCustomerService customerService;

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
        this.customerService = new PagbankCustomerService(
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

    @Override
    public CustomerService customerService() {
        return customerService;
    }

    @Override
    public SubscriptionService subscriptionService() {
        return null;
    }

    @Override
    public InvoiceService invoiceService() {
        return null;
    }
}
