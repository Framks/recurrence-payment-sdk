package com.recurrence.contract;

import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.request.CreatePlanRequest;
import com.recurrence.contract.domain.request.UpdatePlanRequest;
import com.recurrence.contract.infra.PageOptions;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.exception.RecurrenceException;

public interface PlanService {
    Plan create(CreatePlanRequest plan) throws RecurrenceException;
    Plan create(CreatePlanRequest plan, RequestOptions requestOptions) throws RecurrenceException;

    PageResponse<Plan> list(PageOptions pageOptions) throws RecurrenceException;

    Plan get(String planId) throws RecurrenceException;

    Plan update(String planId, UpdatePlanRequest plan) throws RecurrenceException;
    Plan update(String planId, UpdatePlanRequest plan, RequestOptions requestOptions) throws RecurrenceException;

    Void activate(String planId) throws RecurrenceException;
    Void activate(String planId, RequestOptions requestOptions) throws RecurrenceException;

    Void deactivate(String planId) throws RecurrenceException;
    Void deactivate(String planId, RequestOptions requestOptions) throws RecurrenceException;
}