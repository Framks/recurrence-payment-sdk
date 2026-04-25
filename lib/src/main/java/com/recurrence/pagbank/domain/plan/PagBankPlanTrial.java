package com.recurrence.pagbank.domain.plan;

public record PagBankPlanTrial(
        int days,
        Boolean enabled,
        Boolean holdSetupFee
) { }
