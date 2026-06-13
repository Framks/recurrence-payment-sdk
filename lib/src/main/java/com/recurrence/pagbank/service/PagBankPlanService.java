package com.recurrence.pagbank.service;

import com.recurrence.contract.PlanService;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.core.BaseService;
import com.recurrence.pagbank.PagbankConfig;
import com.recurrence.pagbank.client.PagBankPlanClient;
import com.recurrence.pagbank.domain.error.PagBankErrorHandler;
import com.recurrence.pagbank.domain.plan.PagBankPlan;
import com.recurrence.pagbank.mapper.PagBankPlanMapper;
import com.recurrence.pagbank.domain.plan.PagBankListPlan;
import java.net.http.HttpResponse;
import java.util.Map;

import com.recurrence.pagbank.validator.PlanValidator;

import static com.recurrence.constants.ProviderConstants.OPERATION_EXCEPTION;
import static com.recurrence.constants.ProviderConstants.PAGBANK;

public class PagBankPlanService extends BaseService implements PlanService {

    private final PagbankConfig config;
    private final PagBankPlanClient pagBankPlanClient;

    public PagBankPlanService(
            final PagbankConfig config
    ) {
        this.config = config;
        pagBankPlanClient = new PagBankPlanClient(config);
    }

    @Override
    public RequestResponse<Plan> create(Plan plan) {
        return create(plan, RequestOptions.builder().build());
    }

    @Override
    public RequestResponse<Plan> create(Plan plan, RequestOptions requestOptions) {
        try {
            PlanValidator.validateCreateRequest(plan);
            String payload = toJson(PagBankPlanMapper.from(plan));

            var headers = resolveIdempotencyKey(config.fullHeaders(), requestOptions);

            HttpResponse<String> response = pagBankPlanClient.create(payload, headers);

            RequestResponse<Plan> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 201->
                    requestResponse.setBody(
                            PagBankPlanMapper.to(
                                    fromJson(response.body(), PagBankPlan.class)
                            )
                    );
                case 400 ->
                    PagBankErrorHandler.handle(requestResponse, response.body());

                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Plan>> list(Integer offset, Integer limit) {
        try{
            var headers = config.fullHeaders();

            HttpResponse<String> response = pagBankPlanClient.list(pageValue(offset, "offset"), pageValue(limit, "limit"), headers);

            RequestResponse<PageResponse<Plan>> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse.setBody(
                        PagBankPlanMapper.toPageResponse(
                                fromJson(response.body(), PagBankListPlan.class)
                        )
                );
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());

                default -> requestResponse;
            };
        }catch (Exception e){
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Plan> get(String id) {
        try {
            var headers = config.fullHeaders();
            HttpResponse<String> response = pagBankPlanClient.get(id, headers);
            RequestResponse<Plan> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse.setBody(
                        PagBankPlanMapper.to(
                                fromJson(response.body(), PagBankPlan.class)
                        )
                );
                case 400 ->
                        PagBankErrorHandler.handle(requestResponse, response.body());

                default -> requestResponse;
            };
        }catch (Exception e ){
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Plan> update(String id, Plan plan) {
        return update(id, plan, RequestOptions.builder().build());
    }

    @Override
    public RequestResponse<Plan> update(String id, Plan plan, RequestOptions requestOptions) {
        try {
            String payload = toJson(PagBankPlanMapper.from(plan));

            var headers = resolveIdempotencyKey(config.fullHeaders(), requestOptions);

            HttpResponse<String> response = pagBankPlanClient.update(id, payload, headers);

            RequestResponse<Plan> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 200 ->
                        requestResponse.setBody(
                                PagBankPlanMapper.to(
                                        fromJson(response.body(), PagBankPlan.class)
                                )
                        );
                case 400 ->
                        PagBankErrorHandler.handle(requestResponse, response.body());

                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Void> activate(String id) {
        return activate(id, RequestOptions.builder().build());
    }

    @Override
    public RequestResponse<Void> activate(String id, RequestOptions requestOptions) {
        try{
            var headers = resolveIdempotencyKey(config.fullHeaders(), requestOptions);
            HttpResponse<String> response = pagBankPlanClient.activate(id, headers);
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse;
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Void> inactivate(String id) {
        return inactivate(id, RequestOptions.builder().build());
    }

    @Override
    public RequestResponse<Void> inactivate(String id, RequestOptions requestOptions) {
        try{
            var headers = resolveIdempotencyKey(config.fullHeaders(), requestOptions);
            HttpResponse<String> response = pagBankPlanClient.inactivate(id, headers);
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse;
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    private Map<String, String> resolveIdempotencyKey(Map<String, String> headers, RequestOptions requestOptions){
        if(requestOptions.hasIdempotencyKey()){
            headers.put("x-idempotency-key", requestOptions.getIdempotencyKey());
        }
        return headers;
    }
}
