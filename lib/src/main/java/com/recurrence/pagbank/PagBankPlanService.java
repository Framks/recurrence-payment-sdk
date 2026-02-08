package com.recurrence.pagbank;

import com.recurrence.contract.PlanService;
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
    public PagBankPlan create(final PagBankPlan pagBankPlan) {

        final String endpoint = environment.getHost() + "/plans";

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
                pagBankPlan.getReferenceId(),
                pagBankPlan.getName(),
                pagBankPlan.getDescription(),
                pagBankPlan.getAmount().value(),
                pagBankPlan.getAmount().currency(),
                pagBankPlan.getSetupFee(),
                pagBankPlan.getLimitSubscriptions(),
                pagBankPlan.getInterval().length(),
                pagBankPlan.getInterval().unit(),
                pagBankPlan.getBillingCycles(),
                formatPaymentMethods(pagBankPlan.getPaymentMethod()),
                (pagBankPlan.getTrial() != null) ? pagBankPlan.getTrial().days() : 0,
                (pagBankPlan.getTrial() != null) && Boolean.TRUE.equals(pagBankPlan.getTrial().enabled()),
                (pagBankPlan.getTrial() != null) && Boolean.TRUE.equals(pagBankPlan.getTrial().hold_setup_fee())
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

            return pagBankPlan;
        } catch (Exception e) {
            throw new RuntimeException(e);
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
