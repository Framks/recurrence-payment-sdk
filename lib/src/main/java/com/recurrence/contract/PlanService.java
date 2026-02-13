package com.recurrence.contract;

import com.recurrence.contract.domain.Plan;
import java.util.List;

public interface PlanService {
    Plan create(Plan plan);
    List<Plan> list(Long offset, Long limit);
}
