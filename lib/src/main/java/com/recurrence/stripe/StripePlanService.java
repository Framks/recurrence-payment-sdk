package com.recurrence.stripe;

import com.recurrence.contract.PlanService;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;

public class StripePlanService implements PlanService {

    @Override
    public RequestResponse<Plan> create(Plan plan) {

        // ToDo: implementar com base na documentação da Stripe: https://docs.stripe.com/api/plans/create
        return null;
    }

    @Override
    public RequestResponse<PageResponse<Plan>> list(Integer offset, Integer limit) {

        // ToDo: implementar com base na documentação da Stripe: https://docs.stripe.com/api/plans/list
        return null;
    }

    @Override
    public RequestResponse<Plan> get(String id) {
        return null;
    }

    @Override
    public RequestResponse<Plan> update(String id, Plan plan) {
        return null;
    }

    @Override
    public RequestResponse<Void> activate(String id) {
        return null;
    }

    @Override
    public RequestResponse<Void> inactivate(String id) {
        return null;
    }
}
