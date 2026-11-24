package com.recurrence.pagbank.domain.plan;

import com.recurrence.pagbank.domain.commons.ResultSet;

import java.util.List;

public record PagBankListPlan(
        ResultSet resultSet,
        List<PagBankPlan> plans
) {
}
