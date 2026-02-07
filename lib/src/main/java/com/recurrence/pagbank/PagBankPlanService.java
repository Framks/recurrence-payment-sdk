package com.recurrence.pagbank;

import com.recurrence.contract.PlanService;
import com.recurrence.infrastructure.HttpClientAdapter;
import com.recurrence.pagbank.request.payload.Plan;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class PagBankPlanService implements PlanService {

    private PagBankEnvironment environment;
    private String token;
    private HttpClientAdapter httpClient = new HttpClientAdapter();

    public PagBankPlanService(String token) {
        this.token = token;
        this.httpClient = new HttpClientAdapter();
    }

    public PagBankPlanService(
            PagBankEnvironment environment,
            String token
    ) {
        this.environment = environment;
        this.token = token;
    }

    @Override
    public Plan create(Plan plan) {

        String endpoint = environment.getHost() + "/plans";

        String payload = """
                {
                  "reference_id": "%s",
                  "name": "%s",
                  "description": "%s",
                  "amount": {
                    "value": %d,
                    "currency": "%s"
                  },
                  "setup_fee": %d,
                  "limit_subscriptions": %d,
                  "interval": {
                    "length": %d,
                    "unit": "%s"
                  },
                  "billing_cycles": %d,
                  "payment_method": [%s],
                  "trial": {
                    "days": %d,
                    "enabled": %s,
                    "hold_setup_fee": %s
                  }
                }
                """.formatted(
                plan.getReferenceId(),
                plan.getName(),
                plan.getDescription(),
                plan.getAmount().getValue(),
                plan.getAmount().getCurrency(),
                plan.getSetupFee(),
                plan.getLimitSubscriptions(),
                plan.getInterval().getLength(),
                plan.getInterval().getUnit(),
                plan.getBillingCycles(),
                formatPaymentMethods(plan.getPaymentMethod()),
                (plan.getTrial() != null) ? plan.getTrial().getDays() : 0,
                (plan.getTrial() != null) && Boolean.TRUE.equals(plan.getTrial().getEnabled()),
                (plan.getTrial() != null) && Boolean.TRUE.equals(plan.getTrial().getHold_setup_fee())
        );

        Map<String, String> headers = Map.of(
                "Authorization", "Bearer " + token,
                "Content-Type", "application/json"
        );

        try {
            HttpResponse<String> response = httpClient.post(endpoint, payload, headers);
            if (response.statusCode() != 201) {
                throw new RuntimeException(
                        "Erro ao criar plano no PagBank. Status: "
                                + response.statusCode()
                                + " Body: " + response.body()
                );
            }

            return plan;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String formatPaymentMethods(List<String> methods) {
        if (methods == null || methods.isEmpty()) {
            return "";
        }

        return methods.stream()
                .map(method -> "\"" + method + "\"")
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }
}
