package com.recurrence.pagbank.service;

import com.recurrence.contract.PlanService;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.core.BaseService;
import com.recurrence.pagbank.PagBankEnvironment;
import com.recurrence.pagbank.client.PagBankPlanClient;
import com.recurrence.pagbank.domain.error.PagBankErrorHandler;
import com.recurrence.pagbank.domain.plan.PagBankPlan;
import com.recurrence.pagbank.mapper.PagBankPlanMapper;
import com.recurrence.pagbank.domain.plan.PagBankListPlan;
import java.net.http.HttpResponse;
import java.util.Objects;

public class PagBankPlanService extends BaseService implements PlanService {

    private final String token;
    private final PagBankPlanClient pagBankPlanClient;

    public PagBankPlanService(
            final PagBankEnvironment environment,
            final String token
    ) {
        this.token = Objects.requireNonNull(token, "Token must be a valid value");

        pagBankPlanClient = new PagBankPlanClient(Objects.requireNonNull(environment, "Environment must be a valid value"));
    }

    @Override
    public RequestResponse<Plan> create(Plan plan) {
        try {
            // ToDo: nessa etapa seria interessante validar os campos com as regras do pagbank antes de converter para o payload?
            String payload = toJson(PagBankPlanMapper.from(plan));

            var headers = defaultHeaders(token);

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
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Plan>> list(Long offset, Long limit) {
        try{
            var headers = defaultHeaders(token);

            HttpResponse<String> response = pagBankPlanClient.list(offset.intValue(), limit.intValue(), headers);

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
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Plan> get(String id) {
        try {
            var headers = defaultHeaders(token);
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Plan> update(String id, Plan plan) {
        try {
            String payload = toJson(PagBankPlanMapper.from(plan));

            var headers = defaultHeaders(token);

            HttpResponse<String> response = pagBankPlanClient.update(id, payload, headers);

            RequestResponse<Plan> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 200, 201 -> // ToDo: validar se o pagbank retorna 200 ou 201 para update
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Void> activate(String id) {
        try{
            var headers = defaultHeaders(token);
            HttpResponse<String> response = pagBankPlanClient.activate(id, headers);
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()) {
                case 200, 204 -> requestResponse; // ToDo: validar se o pagbank retorna 200 ou 204 para inactivate
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Void> inactivate(String id) {
        try{
            var headers = defaultHeaders(token);
            HttpResponse<String> response = pagBankPlanClient.inactivate(id, headers);
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()) {
                case 200, 204 -> requestResponse; // ToDo: validar se o pagbank retorna 200 ou 204 para inactivate
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
