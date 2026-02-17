package com.recurrence.contract;

import com.recurrence.contract.domain.ListPlan;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.infra.RequestResponse;

public interface PlanService {
    RequestResponse<Plan> create(Plan plan);

    RequestResponse<ListPlan> list(Long offset, Long limit);

    RequestResponse<Plan> get(String id);

    RequestResponse<Plan> update(String id, Plan plan);

    RequestResponse<Void> activate(String id);

    RequestResponse<Void> inactivate(String id);
}
