package com.recurrence.pagbank;

import com.recurrence.contract.CouponService;
import com.recurrence.contract.CustomerService;
import com.recurrence.contract.InvoiceService;
import com.recurrence.contract.PlanService;
import com.recurrence.contract.PreferencesService;
import com.recurrence.contract.RecurrenceProvider;
import com.recurrence.contract.SubscriptionService;
import com.recurrence.pagbank.service.PagBankCouponService;
import com.recurrence.pagbank.service.PagBankInvoiceService;
import com.recurrence.pagbank.service.PagBankPlanService;
import com.recurrence.pagbank.service.PagBankPreferencesService;
import com.recurrence.pagbank.service.PagBankSubscriptionService;
import com.recurrence.pagbank.service.PagbankCustomerService;

public class PagBankProvider implements RecurrenceProvider {

    private final PagBankPlanService planService;
    private final PagBankCouponService couponService;
    private final PagbankCustomerService customerService;
    private final SubscriptionService subscriptionService;
    private final PagBankInvoiceService invoiceService;
    private final PagBankPreferencesService preferencesService;

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
        this.subscriptionService = new PagBankSubscriptionService(
            environment,
            token
        );
        this.invoiceService = new PagBankInvoiceService(
            environment,
            token
        );
        this.preferencesService = new PagBankPreferencesService(
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
        return subscriptionService;
    }

    @Override
    public InvoiceService invoiceService() {
        return invoiceService;
    }

    @Override
    public PreferencesService preferencesService() {
        return preferencesService;
    }
}
