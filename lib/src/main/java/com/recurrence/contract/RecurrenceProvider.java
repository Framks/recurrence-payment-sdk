package com.recurrence.contract;

public interface RecurrenceProvider {
    PlanService planService();
    CouponService couponService();
    CustomerService customerService();
    SubscriptionService subscriptionService();
    InvoiceService invoiceService();
    PreferencesService preferencesService();
}
