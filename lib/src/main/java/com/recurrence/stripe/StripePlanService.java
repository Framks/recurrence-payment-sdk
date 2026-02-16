package com.recurrence.stripe;

import com.recurrence.contract.PlanService;
import com.recurrence.contract.domain.ListPlan;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.infra.RequestResponse;
import java.util.List;

public class StripePlanService implements PlanService {

    @Override
    public RequestResponse<Plan> create(Plan plan) {
        return null;
    }

    @Override
    public RequestResponse<ListPlan> list(Long offset, Long limit) {
        return null;
    }
}
