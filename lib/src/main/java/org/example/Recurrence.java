package org.example;

import org.example.pagbank.PagBankEnvironment;

public class Recurrence {

    private final RecurrenceProvider provider;

    private Recurrence(RecurrenceProvider provider) {
        this.provider = provider;
    }

    public PlanService plan() {
        return provider.planService();
    }

    // Entry points por provider
    public static class PagBank {
        public static Recurrence config(PagBankEnvironment environment, String token) {
            return new Recurrence(
                    new PagBankProvider(environment, token)
            );
        }
    }
}

