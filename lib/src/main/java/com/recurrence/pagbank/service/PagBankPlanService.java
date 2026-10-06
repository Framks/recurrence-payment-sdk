package com.recurrence.pagbank.service;

import com.recurrence.contract.PlanService;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.request.CreatePlanRequest;
import com.recurrence.contract.domain.request.UpdatePlanRequest;
import com.recurrence.contract.filters.PageOptions;
import com.recurrence.contract.filters.PlanFilter;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.core.BaseService;
import com.recurrence.core.ProviderContext;
import com.recurrence.core.QueryParams;
import com.recurrence.core.RequestValidator;
import com.recurrence.pagbank.constant.Constant;
import com.recurrence.pagbank.domain.plan.PagBankListPlan;
import com.recurrence.pagbank.domain.plan.PagBankPlan;
import com.recurrence.pagbank.mapper.PagBankFilterMapper;
import com.recurrence.pagbank.mapper.PagBankPlanMapper;

import static com.recurrence.infrastructure.MethodRequest.GET;
import static com.recurrence.infrastructure.MethodRequest.POST;
import static com.recurrence.infrastructure.MethodRequest.PUT;
import static com.recurrence.pagbank.constant.Constant.*;

public class PagBankPlanService extends BaseService implements PlanService {

    private static final String PLANS = "/plans";

    public PagBankPlanService(ProviderContext context) {
        super(context);
    }

    @Override
    public Plan create(CreatePlanRequest plan, RequestOptions requestOptions) {
        RequestValidator.validate(plan);
        return execute(
                Operation.PLAN_CREATE,
                request(POST, PLANS, requestOptions)
                        .body(toJson(Operation.PLAN_CREATE, PagBankPlanMapper.toRequest(plan)))
                        .build(),
                PagBankPlan.class,
                PagBankPlanMapper::toContract
        );
    }

    @Override
    public Plan get(String planId) {
        RequestValidator.requireId(planId, PLAN_ID);
        return execute(
                Operation.PLAN_GET,
                request(GET, planPath(planId), null).build(),
                PagBankPlan.class,
                PagBankPlanMapper::toContract
        );
    }

    @Override
    public PageResponse<Plan> list(PageOptions pageOptions, PlanFilter planFilter) {
        QueryParams query = QueryParams.create().page(pageOptions);
        if (planFilter != null) {
            query.add(STATUS, PagBankFilterMapper.status(planFilter.getStatus()))
                    .add(REFERENCE_ID, planFilter.getQuery());
        }
        return execute(
                Operation.PLAN_LIST,
                request(GET, query.appendTo(PLANS), null).build(),
                PagBankListPlan.class,
                PagBankPlanMapper::toPage
        );
    }

    @Override
    public Plan update(String planId, UpdatePlanRequest plan, RequestOptions requestOptions) {
        RequestValidator.requireId(planId, PLAN_ID);
        RequestValidator.validate(plan);
        return execute(
                Constant.Operation.PLAN_UPDATE,
                request(PUT, planPath(planId), requestOptions)
                        .body(toJson(Constant.Operation.PLAN_UPDATE, PagBankPlanMapper.toRequest(plan)))
                        .build(),
                PagBankPlan.class,
                PagBankPlanMapper::toContract
        );
    }

    @Override
    public void activate(String planId, RequestOptions requestOptions) {
        RequestValidator.requireId(planId, PLAN_ID);
        execute(Operation.PLAN_ACTIVATE, request(PUT, planPath(planId) + PATH_ACTIVE, requestOptions).build());
    }

    @Override
    public void deactivate(String planId, RequestOptions requestOptions) {
        RequestValidator.requireId(planId, PLAN_ID);
        execute(Operation.PLAN_DEACTIVATE, request(PUT, planPath(planId) + PATH_INACTIVE, requestOptions).build());
    }

    private static String planPath(String planId) {
        return PLANS + "/" + segment(planId);
    }
}
