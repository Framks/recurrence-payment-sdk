package com.recurrence.pagbank;

import com.recurrence.contract.PlanService;
import com.recurrence.contract.RecurrenceProvider;

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
