package com.recurrence.contract;

import com.recurrence.exception.RecurrenceException;

public interface RecurrenceProvider {
    PlanService planService() throws RecurrenceException;
    CouponService couponService() throws RecurrenceException;
    CustomerService customerService() throws RecurrenceException;
    SubscriptionService subscriptionService() throws RecurrenceException;
    InvoiceService invoiceService() throws RecurrenceException;
    PreferencesService preferencesService() throws RecurrenceException;
}
