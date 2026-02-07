package com.recurrence;

import com.recurrence.contract.Recurrence;
import com.recurrence.pagbank.PagBankEnvironment;
import com.recurrence.pagbank.request.payload.Amount;
import com.recurrence.pagbank.request.payload.Interval;
import com.recurrence.pagbank.request.payload.Plan;

import java.util.List;
import java.util.UUID;

public class Library {

    public static void main(String[] args) {
        Recurrence.PagBank.config(PagBankEnvironment.SANDBOX, "seu_token_aqui")
                .plan().create(createPlan());
    }

    private static Plan createPlan() {
        return Plan.builder()
                .name("Nome do plano sdk")
                .referenceId(UUID.randomUUID().toString())
                .amount(new Amount(500))
                .description("Descrição do plano")
                .interval(new Interval(1))
                .paymentMethod(List.of("BOLETO"))
                .billingCycles(2)
                .limitSubscriptions(5)
                .setupFee(100)
                .build();
    }
}
