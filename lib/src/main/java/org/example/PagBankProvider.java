package org.example;

import org.example.pagbank.PagBankEnvironment;

public class PagBankProvider implements RecurrenceProvider {

    private final PagBankPlanService planService;

    public PagBankProvider(
            PagBankEnvironment environment,
            String token
    ) {
        this.planService = new PagBankPlanService(
                environment,
                token
        );
    }

    @Override
    public PlanService planService() {
        return planService;
    }
}
