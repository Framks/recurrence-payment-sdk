package com.recurrence.stripe;

import com.recurrence.contract.CouponService;
import com.recurrence.contract.CustomerService;
import com.recurrence.contract.InvoiceService;
import com.recurrence.contract.PlanService;
import com.recurrence.contract.RecurrenceProvider;
import com.recurrence.contract.SubscriptionService;

public class StripeProvider implements RecurrenceProvider {

    @Override
    public PlanService planService() {
        return null;
    }

    @Override
    public CouponService couponService() {
        return null;
    }

    @Override
    public CustomerService customerService() {
        return null;
    }

    @Override
    public SubscriptionService subscriptionService() {
        return null;
    }

    @Override
    public InvoiceService invoiceService() {
        return null;
    }

    @Override
    public com.recurrence.contract.PreferencesService preferencesService() {
        return null;
    }

}
