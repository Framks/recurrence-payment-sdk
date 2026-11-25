package com.recurrence.contract.domain.commons;

import com.recurrence.contract.domain.enums.PlanIntervalType;

public record PlanInterval(
        PlanIntervalType type,
        Integer frequency
) { }
