package com.recurrence;

import com.recurrence.contract.Recurrence;
import com.recurrence.contract.domain.Plan;
import com.recurrence.pagbank.PagBankEnvironment;

import java.util.List;
import java.util.UUID;

public class Library {

    static void main(String[] args) {

//        var pagBankRecurence = Recurrence.PagBank.config(
//                PagBankEnvironment.SANDBOX,
//                "50b2e630-a102-46f9-9650-f9152da588bf6d0aeca44496af93c486dcf03e81dd393a81-c41b-4d86-b6cc-708ea2c202b7");
//
//
//        var pagBankPlan = Recurrence.PagBank.config(
//                        PagBankEnvironment.SANDBOX,
//                        "seu_token_aqui")
//                .plan().create(createPagBankPlan());
//
//        System.out.println(pagBankPlan);

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
