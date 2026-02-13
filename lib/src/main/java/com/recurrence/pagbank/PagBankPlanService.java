package com.recurrence.pagbank;

import com.recurrence.contract.PlanService;
import com.recurrence.contract.domain.Plan;
import com.recurrence.infrastructure.HttpClientAdapter;
import com.recurrence.pagbank.domain.plan.PagBankPlan;

import com.recurrence.pagbank.dto.PagBankListPlan;
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

    @Override
    public List<Plan> list(Long offset, Long limit) {
        final String endpoint = environment.getHost() + "/plans?offset=" + offset + "&limit=" + limit;

        Map<String, String> headers = Map.of(
                "Authorization", "Bearer " + token,
                "Content-Type", "application/json"
        );

        try{
            PagBankListPlan result = httpClient.get(endpoint, headers, PagBankListPlan.class);
            System.out.println(result.toString());
            return  result.getPlans().stream()
                    .map(PagBankPlanMapper::to)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Fail to list plans "+e);
        }
    }
}
