package com.recurrence.contract;

import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.infra.RequestResponse;

public interface PlanService {
    RequestResponse<Plan> create(Plan plan);
}
