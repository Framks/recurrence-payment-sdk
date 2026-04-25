package com.recurrence.contract;

import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;

public interface PlanService {
    RequestResponse<Plan> create(Plan plan) throws Exception;
    RequestResponse<PageResponse<Plan>> list(Long offset, Long limit) throws Exception;
    RequestResponse<Plan> get(String id) throws Exception;
    RequestResponse<Plan> update(String id, Plan plan) throws Exception;
    RequestResponse<Void> activate(String id) throws Exception;
    RequestResponse<Void> inactivate(String id) throws Exception;
}
