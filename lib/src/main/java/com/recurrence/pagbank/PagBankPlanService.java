package com.recurrence.pagbank;

import com.recurrence.core.Converter;
import com.recurrence.contract.PlanService;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.infra.RequestResponse;
import com.recurrence.infrastructure.HttpClientAdapter;
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
    private final HttpClientAdapter httpClient = new HttpClientAdapter();

    public PagBankPlanService(
            final PagBankEnvironment environment,
            final String token
    ) {
        this.environment = Objects.requireNonNull(environment, "Environment must be a valid value");
        this.token = Objects.requireNonNull(token, "Token must be a valid value");
    }

    @Override
    public RequestResponse<Plan> create(Plan plan) {

        try {

            PagBankPlan pagBankPlan = PagBankPlanMapper.from(plan);

            final String endpoint = environment.getHost() + "/plans";

            String payload = Converter.mapper().writeValueAsString(pagBankPlan);


            Map<String, String> headers = Map.of(
                    "Authorization", "Bearer " + token,
                    "Content-Type", "application/json"
            );

            HttpResponse<String> response = httpClient.post(endpoint, payload, headers);
            RequestResponse requestResponse = createRequestResponse(plan, response);

            if (response.statusCode() != 201) {

                if (response.statusCode() == 400) {
                    var pagBankError = Converter.mapper().readValue(response.body(), PagBankErrorList.class);
                    var errorList = PagBankErrorMapper.to(pagBankError);
                    requestResponse.setErrors(errorList);
                    return requestResponse;
                }
            }

            var pagBankPlanResponse = Converter.mapper().readValue(response.body(), PagBankPlan.class);
            var planResponse = PagBankPlanMapper.to(pagBankPlanResponse);

            requestResponse.setBody(planResponse);

            return requestResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private RequestResponse createRequestResponse(Plan plan, HttpResponse<String> response) {
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
