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
}
