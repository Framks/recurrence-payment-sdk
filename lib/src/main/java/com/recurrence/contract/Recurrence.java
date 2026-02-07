package com.recurrence.contract;

import com.recurrence.pagbank.PagBankEnvironment;
import com.recurrence.pagbank.PagBankProvider;

public class Recurrence {

    private final RecurrenceProvider provider;

    private Recurrence(RecurrenceProvider provider) {
        this.provider = provider;
    }

    public PlanService plan() {
        return provider.planService();
    }

    public static class PagBank {
        public static Recurrence config(PagBankEnvironment environment, String token) {
            return new Recurrence(
                    new PagBankProvider(environment, token)
            );
        }
    }
}

