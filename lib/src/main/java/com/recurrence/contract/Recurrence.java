package com.recurrence.contract;

import com.recurrence.pagbank.PagBankEnvironment;
import com.recurrence.pagbank.PagBankProvider;
import com.recurrence.stripe.StripeProvider;

final public class Recurrence {

    private final RecurrenceProvider provider;

    private Recurrence(final RecurrenceProvider provider) {
        this.provider = provider;
    }

    public PlanService plan() {
        return provider.planService();
    }

    public CouponService coupon() {
        return provider.couponService();
    }

    public CustomerService customer(){
        return provider.customerService();
    }

    public SubscriptionService subscription() {
        return provider.subscriptionService();
    }

    public InvoiceService invoice() {
        return provider.invoiceService();
    }

    public static class PagBank {
        public static Recurrence config(final PagBankEnvironment environment, final String token) {
            return new Recurrence(
                    new PagBankProvider(environment, token)
            );
        }
    }

    public static class Stripe {
        public static Recurrence config() {
            return new Recurrence(
                    new StripeProvider()
            );
        }
    }

    // ToDo: implementar outros providers, como Vindi, PayPal e Iugu, com base na documentação de cada um:
    // https://vindi.github.io/api-docs/dist/,
    // https://developer.paypal.com/docs/api/subscriptions/v1/,
    // https://dev.iugu.com/docs/realizar-cobran%C3%A7as-recorrentes-por-api
}
