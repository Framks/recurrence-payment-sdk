package com.recurrence.pagbank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recurrence.contract.PlanService;
import com.recurrence.contract.domain.ListPlan;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.infra.RequestResponse;
import com.recurrence.core.Converter;
import com.recurrence.pagbank.domain.error.PagBankErrorList;
import com.recurrence.pagbank.domain.error.PagBankErrorMapper;
import com.recurrence.pagbank.domain.plan.PagBankPlan;
import com.recurrence.pagbank.domain.plan.PagBankPlanMapper;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PagBankPlanService implements PlanService {

    private final PagBankEnvironment environment;
    private final String token;
    private final PagBankPlanClient pagBankPlanClient;

    public PagBankPlanService(
            final PagBankEnvironment environment,
            final String token
    ) {
        this.environment = Objects.requireNonNull(environment, "Environment must be a valid value");
        this.token = Objects.requireNonNull(token, "Token must be a valid value");

        pagBankPlanClient = new PagBankPlanClient(environment);
    }

    @Override
    public RequestResponse<Plan> create(Plan plan) {
        try {
            String payload = createPayload(plan);

            var headers = createHeaders(token);

            HttpResponse<String> response = pagBankPlanClient.create(payload, headers);

            RequestResponse requestResponse = createRequestResponse(response);

            switch (requestResponse.getHttpResponse()) {
                case 201:
                    createSuccessResponse(requestResponse, response);
                    break;
                case 400:
                    createErrorResponse(requestResponse, response);
                    break;
            }

            return requestResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<ListPlan> list(Long offset, Long limit) {
        return null;
    }

    private static String createPayload(Plan plan) throws JsonProcessingException {
        return Converter.mapper().writeValueAsString(PagBankPlanMapper.from(plan));
    }

    private RequestResponse createErrorResponse(RequestResponse requestResponse, HttpResponse<String> response) throws JsonProcessingException {
        return requestResponse.setErrors(PagBankErrorMapper.to(Converter.mapper().readValue(response.body(), PagBankErrorList.class)));
    }

    private RequestResponse createSuccessResponse(RequestResponse requestResponse, HttpResponse<String> response) throws JsonProcessingException {
        return requestResponse.setBody(PagBankPlanMapper.to(Converter.mapper().readValue(response.body(), PagBankPlan.class)));
    }

    private Map<String, String> createHeaders(final String token) {
        return Map.of(
                "Authorization", "Bearer " + token,
                "Content-Type", "application/json"
        );
    }

    private RequestResponse createRequestResponse(HttpResponse<String> response) {
        return new RequestResponse<>(
                response.statusCode(),
                getHeaders(response)
        );
    }

    private String formatPaymentMethods(final List<String> methods) {
        if (methods == null || methods.isEmpty()) {
            return "";
        }

        return methods.stream()
                .map(method -> "\"" + method + "\"")
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }

    private List<Map<String, String>> getHeaders(HttpResponse<String> response) {
        return response.headers()
                .map()
                .entrySet()
                .stream()
                .flatMap(entry -> entry.getValue()
                        .stream()
                        .map(value -> {
                            Map<String, String> headerMap = new HashMap<>();
                            headerMap.put(entry.getKey(), value);
                            return headerMap;
                        })
                )
                .toList();
    }
}
