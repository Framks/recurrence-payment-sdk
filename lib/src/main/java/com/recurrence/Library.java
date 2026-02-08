package com.recurrence;

import com.recurrence.contract.Recurrence;
import com.recurrence.pagbank.PagBankEnvironment;
import com.recurrence.pagbank.domain.commons.Amount;
import com.recurrence.pagbank.domain.plan.PagBankPlan;
import com.recurrence.pagbank.domain.plan.PagBankPlanInterval;

import java.util.List;
import java.util.UUID;

public class Library {

    static void main(String[] args) {
        Recurrence.PagBank.config(
                        PagBankEnvironment.SANDBOX,
                        "seu_token_aqui")
                .plan().create(createPlan());
    }

    private static PagBankPlan createPlan() {
        return PagBankPlan.builder()
                .name("Nome do plano sdk")
                .referenceId(UUID.randomUUID().toString())
                .amount(new Amount(500))
                .description("Descrição do plano")
                .interval(new PagBankPlanInterval(1, "MONTH"))
                .paymentMethod(List.of("BOLETO"))
                .billingCycles(2)
                .limitSubscriptions(5)
                .setupFee(100)
                .build();
    }
}
