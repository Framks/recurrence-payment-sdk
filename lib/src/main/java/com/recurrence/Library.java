package com.recurrence;

import com.recurrence.contract.Recurrence;
import com.recurrence.contract.domain.Plan;
import com.recurrence.pagbank.PagBankEnvironment;

import java.util.List;
import java.util.UUID;

public class Library {

    private static final String TOKEN = "";

    public static void main(String[] args) {

        var recurrence = Recurrence.PagBank.config(
                PagBankEnvironment.SANDBOX,
                TOKEN
        );

        var pagBankPlan = recurrence.plan().create(createPagBankPlan());

        if (pagBankPlan.getBody() != null) {
            System.out.println(pagBankPlan.getBody().getId());
        } else if (pagBankPlan.getErrors() != null) {
            System.out.println(pagBankPlan.getErrors().errors().get(0).errorKey());
            System.out.println(pagBankPlan.getErrors().errors().get(0).message());
            System.out.println(pagBankPlan.getErrors().errors().get(0).description());
        }
    }

    private static Plan createPagBankPlan() {

        return Plan.builder()
                .name("Nome do plano sdk")
                .referenceId(UUID.randomUUID().toString())
                .amount(500, "BR")
                .description("Descrição do plano")
                .interval(1, "MONTH")
                .paymentMethods(List.of("BOLETO", "CREDIT_CARD"))
                .billingCycles(2)
                .limitSubscriptions(5)
                .setupFee(100)
                .build();
    }
}
