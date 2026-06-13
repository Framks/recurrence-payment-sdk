package com.recurrence.pagbank;

import com.recurrence.contract.CouponService;
import com.recurrence.contract.CustomerService;
import com.recurrence.contract.InvoiceService;
import com.recurrence.contract.PlanService;
import com.recurrence.contract.PreferencesService;
import com.recurrence.contract.RecurrenceProvider;
import com.recurrence.contract.SubscriptionService;
import com.recurrence.contract.config.Environment;
import com.recurrence.contract.config.ProviderConfig;
import com.recurrence.exception.RecurrenceException;
import com.recurrence.pagbank.service.PagBankCouponService;
import com.recurrence.pagbank.service.PagBankInvoiceService;
import com.recurrence.pagbank.service.PagBankPlanService;
import com.recurrence.pagbank.service.PagBankPreferencesService;
import com.recurrence.pagbank.service.PagBankSubscriptionService;
import com.recurrence.pagbank.service.PagbankCustomerService;

import static com.recurrence.constants.ErrorMessage.PAGBANK_AUTHENTICATION_HEADERS_TOKEN;
import static com.recurrence.constants.ErrorMessage.PAGBANK_NOT_FOUND_TOKEN;

public class PagBankProvider implements RecurrenceProvider {

    private final PagBankPlanService planService;
    private final PagBankCouponService couponService;
    private final PagbankCustomerService customerService;
    private final SubscriptionService subscriptionService;
    private final PagBankInvoiceService invoiceService;
    private final PagBankPreferencesService preferencesService;

    public PagBankProvider(
            Environment environment,
            ProviderConfig config
    ){
        validateConfig(config);
        final PagbankConfig pagBankconfig = new PagbankConfig(environment, config);

        this.planService = new PagBankPlanService(pagBankconfig);
        this.couponService = new PagBankCouponService(pagBankconfig);
        this.customerService = new PagbankCustomerService(pagBankconfig);
        this.subscriptionService = new PagBankSubscriptionService(pagBankconfig);
        this.invoiceService = new PagBankInvoiceService(pagBankconfig);
        this.preferencesService = new PagBankPreferencesService(pagBankconfig);
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

    private void validateConfig(ProviderConfig config){
        if ( config.getToken().isBlank() ) throw new RecurrenceException(PAGBANK_NOT_FOUND_TOKEN);
        if ( config.getDefaultHeaders().containsKey("Authentication") || config.getDefaultHeaders().containsKey("authentication") ) throw new RecurrenceException(PAGBANK_AUTHENTICATION_HEADERS_TOKEN);
    }
}
