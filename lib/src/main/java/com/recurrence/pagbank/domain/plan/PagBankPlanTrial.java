package com.recurrence.pagbank.domain.plan;

public record PagBankPlanTrial(
        Integer days,
        Boolean enabled,
        Boolean holdSetupFee
) { }
