package com.recurrence.pagbank.domain.plan;

import com.recurrence.pagbank.domain.commons.PagBankAmount;

import java.time.OffsetDateTime;
import java.util.List;

/** Plano devolvido pelo PagBank ({@code /plans}). */
public record PagBankPlan(
        String id,
        String referenceId,
        String status,
        String name,
        String description,
        PagBankAmount amount,
        PagBankPlanInterval interval,
        Integer billingCycles,
        PagBankPlanTrial trial,
        List<String> paymentMethod,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
