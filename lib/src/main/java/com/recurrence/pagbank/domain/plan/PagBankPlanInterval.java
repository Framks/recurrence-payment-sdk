package com.recurrence.pagbank.domain.plan;

public record PagBankPlanInterval(
        int length,
        String unit
) {
    public PagBankPlanInterval(final int length) {
        this(length, "MONTH");
    }
}
