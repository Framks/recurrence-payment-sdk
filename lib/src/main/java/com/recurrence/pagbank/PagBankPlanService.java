package com.recurrence.pagbank;

import com.recurrence.contract.PlanService;
import com.recurrence.contract.domain.Plan;
import com.recurrence.infrastructure.HttpClientAdapter;
import com.recurrence.pagbank.domain.plan.PagBankPlan;

import java.net.http.HttpResponse;
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
    public Plan create(Plan plan) {

        PagBankPlan pagBankPlan = PagBankPlanMapper.from(plan);

        final String endpoint = environment.getHost() + "/plans";

        Map<String, String> headers = Map.of(
                "Authorization", "Bearer " + token,
                "Content-Type", "application/json"
        );

        try {
            PagBankPlan response = httpClient.post(endpoint, pagBankPlan, headers, PagBankPlan.class);

            plan.setId(response.getId());

            return plan;
        } catch (Exception e) {
            throw new RuntimeException("Fail to create plan "+e);
        }
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
}
