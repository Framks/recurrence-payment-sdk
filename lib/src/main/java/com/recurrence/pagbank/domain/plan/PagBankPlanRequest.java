package com.recurrence.pagbank.domain.plan;

import com.recurrence.pagbank.domain.commons.PagBankAmount;

import java.util.List;

/** Corpo de criação/atualização de plano no PagBank. Campos nulos não são enviados. */
public record PagBankPlanRequest(
        String name,
        PagBankAmount amount,
        PagBankPlanInterval interval,
        Integer billingCycles,
        PagBankPlanTrial trial,
        List<String> paymentMethod
) {
}
