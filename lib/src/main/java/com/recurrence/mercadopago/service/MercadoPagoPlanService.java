package com.recurrence.mercadopago.service;

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
import com.recurrence.mercadopago.domain.commons.MercadoPagoStatusRequest;
import com.recurrence.mercadopago.domain.plan.MercadoPagoPlan;
import com.recurrence.mercadopago.domain.plan.MercadoPagoPlanList;
import com.recurrence.mercadopago.mapper.MercadoPagoFilterMapper;
import com.recurrence.mercadopago.mapper.MercadoPagoPlanMapper;

import static com.recurrence.infrastructure.MethodRequest.GET;
import static com.recurrence.infrastructure.MethodRequest.POST;
import static com.recurrence.infrastructure.MethodRequest.PUT;
import static com.recurrence.mercadopago.constant.Constant.Operation;
import static com.recurrence.mercadopago.constant.Constant.PATH_SEARCH;
import static com.recurrence.mercadopago.constant.Constant.PLANS;
import static com.recurrence.mercadopago.constant.Constant.PLAN_ID;
import static com.recurrence.mercadopago.constant.Constant.QUERY;
import static com.recurrence.mercadopago.constant.Constant.STATUS;
import static com.recurrence.mercadopago.constant.Constant.Status.PLAN_ACTIVE;
import static com.recurrence.mercadopago.constant.Constant.Status.PLAN_CANCELLED;

public class MercadoPagoPlanService extends BaseService implements PlanService {

    public MercadoPagoPlanService(ProviderContext context) {
        super(context);
    }

    @Override
    public Plan create(CreatePlanRequest plan, RequestOptions requestOptions) {
        RequestValidator.validate(plan);
        return execute(
                Operation.PLAN_CREATE,
                request(POST, PLANS, requestOptions)
                        .body(toJson(Operation.PLAN_CREATE, MercadoPagoPlanMapper.toRequest(plan, context.config().getBackUrl())))
                        .build(),
                MercadoPagoPlan.class,
                MercadoPagoPlanMapper::toContract
        );
    }

    @Override
    public Plan get(String planId) {
        RequestValidator.requireId(planId, PLAN_ID);
        return execute(
                Operation.PLAN_GET,
                request(GET, planPath(planId), null).build(),
                MercadoPagoPlan.class,
                MercadoPagoPlanMapper::toContract
        );
    }

    @Override
    public PageResponse<Plan> list(PageOptions pageOptions, PlanFilter planFilter) {
        QueryParams query = QueryParams.create().page(pageOptions);
        if (planFilter != null) {
            query.add(STATUS, MercadoPagoFilterMapper.planStatus(planFilter.getStatus()))
                    .add(QUERY, planFilter.getQuery());
        }
        return execute(
                Operation.PLAN_LIST,
                request(GET, query.appendTo(PLANS + PATH_SEARCH), null).build(),
                MercadoPagoPlanList.class,
                MercadoPagoPlanMapper::toPage
        );
    }

    @Override
    public Plan update(String planId, UpdatePlanRequest plan, RequestOptions requestOptions) {
        RequestValidator.requireId(planId, PLAN_ID);
        RequestValidator.validate(plan);
        return execute(
                Operation.PLAN_UPDATE,
                request(PUT, planPath(planId), requestOptions)
                        .body(toJson(Operation.PLAN_UPDATE, MercadoPagoPlanMapper.toRequest(plan)))
                        .build(),
                MercadoPagoPlan.class,
                MercadoPagoPlanMapper::toContract
        );
    }

    @Override
    public void activate(String planId, RequestOptions requestOptions) {
        changeStatus(Operation.PLAN_ACTIVATE, planId, PLAN_ACTIVE, requestOptions);
    }

    @Override
    public void deactivate(String planId, RequestOptions requestOptions) {
        changeStatus(Operation.PLAN_DEACTIVATE, planId, PLAN_CANCELLED, requestOptions);
    }

    /** O Mercado Pago não tem endpoint de ação: o estado muda pelo {@code PUT} do plano. */
    private void changeStatus(String operation, String planId, String status, RequestOptions requestOptions) {
        RequestValidator.requireId(planId, PLAN_ID);
        execute(
                operation,
                request(PUT, planPath(planId), requestOptions)
                        .body(toJson(operation, new MercadoPagoStatusRequest(status)))
                        .build()
        );
    }

    private static String planPath(String planId) {
        return PLANS + "/" + segment(planId);
    }
}
