package com.recurrence;

import com.recurrence.contract.Recurrence;
import com.recurrence.contract.domain.Plan;
import com.recurrence.pagbank.PagBankEnvironment;

import java.util.List;
import java.util.UUID;

public class Library {

    static void main(String[] args) {
        var pagBankPlan = Recurrence.PagBank.config(
                        PagBankEnvironment.SANDBOX,
                        "seu_token_aqui")
                .plan().create(createPagBankPlan());

        var stripePlan = Recurrence.Stripe.config()
                .plan()
                .create(createPagBankPlan());

        System.out.println(pagBankPlan);

    }

    private static Plan createPagBankPlan() {

        return Plan.builder()
                .name("Nome do plano sdk")
                .referenceId(UUID.randomUUID().toString())
                .amount(500, "BRL")
                .description("Descrição do plano")
                .interval(1, "MONTH")
                .paymentMethods(List.of("BOLETO", "CREDIT_CARD"))
                .billingCycles(2)
                .limitSubscriptions(5)
                .setupFee(100)
                .build();
    }
}
