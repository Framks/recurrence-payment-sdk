package com.recurrence.stripe;

import com.recurrence.contract.PlanService;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.infra.RequestResponse;

public class StripePlanService implements PlanService {

    @Override
    public RequestResponse<Plan> create(Plan plan) {
        return null;
    }
}
