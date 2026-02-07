package com.recurrence.pagbank.domain.plan;

public record PagBankPlanTrial(
        int days,
        Boolean enabled,
        Boolean hold_setup_fee
) { }
