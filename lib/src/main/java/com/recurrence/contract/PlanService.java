package com.recurrence.contract;

import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.exception.RecurrenceException;

public interface PlanService {
    RequestResponse<Plan> create(Plan plan) throws RecurrenceException;
    RequestResponse<Plan> create(Plan plan, RequestOptions requestOptions) throws RecurrenceException;

    // AQUI NO LIST DEVEMOS FAZER O FILTER tipo PlanFilter
    RequestResponse<PageResponse<Plan>> list(Integer offset, Integer limit) throws RecurrenceException;

    RequestResponse<Plan> get(String id) throws RecurrenceException;

    RequestResponse<Plan> update(String id, Plan plan) throws RecurrenceException;
    RequestResponse<Plan> update(String id, Plan plan, RequestOptions requestOptions) throws RecurrenceException;

    RequestResponse<Void> activate(String id) throws RecurrenceException;
    RequestResponse<Void> activate(String id, RequestOptions requestOptions) throws RecurrenceException;

    RequestResponse<Void> inactivate(String id) throws RecurrenceException;
    RequestResponse<Void> inactivate(String id, RequestOptions requestOptions) throws RecurrenceException;
}
