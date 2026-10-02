package com.recurrence.contract;

import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.request.CreatePlanRequest;
import com.recurrence.contract.domain.request.UpdatePlanRequest;
import com.recurrence.contract.filters.PlanFilter;
import com.recurrence.contract.filters.PageOptions;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.exception.RecurrenceException;

/**
 * Operações de plano comuns aos provedores.
 *
 * <p>Cada operação tem um único método abstrato; as variações sem
 * {@link RequestOptions} ou sem filtro são {@code default}.</p>
 */
public interface PlanService {

    default Plan create(CreatePlanRequest plan) throws RecurrenceException {
        return create(plan, RequestOptions.empty());
    }

    Plan create(CreatePlanRequest plan, RequestOptions requestOptions) throws RecurrenceException;

    Plan get(String planId) throws RecurrenceException;

    default PageResponse<Plan> list(PageOptions pageOptions) throws RecurrenceException {
        return list(pageOptions, null);
    }

    /**
     * @param pageOptions paginação; {@code null} usa o padrão do provedor
     * @param planFilter  filtros; {@code null} não filtra
     */
    PageResponse<Plan> list(PageOptions pageOptions, PlanFilter planFilter) throws RecurrenceException;

    default Plan update(String planId, UpdatePlanRequest plan) throws RecurrenceException {
        return update(planId, plan, RequestOptions.empty());
    }

    Plan update(String planId, UpdatePlanRequest plan, RequestOptions requestOptions) throws RecurrenceException;

    default void activate(String planId) throws RecurrenceException {
        activate(planId, RequestOptions.empty());
    }

    void activate(String planId, RequestOptions requestOptions) throws RecurrenceException;

    default void deactivate(String planId) throws RecurrenceException {
        deactivate(planId, RequestOptions.empty());
    }

    void deactivate(String planId, RequestOptions requestOptions) throws RecurrenceException;
}
