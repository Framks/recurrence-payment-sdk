package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.pagbank.domain.commons.PagBankAmount;
import com.recurrence.pagbank.domain.plan.PagBankListPlan;
import com.recurrence.pagbank.domain.plan.PagBankPlan;
import com.recurrence.pagbank.domain.plan.PagBankPlanInterval;
import com.recurrence.pagbank.domain.plan.PagBankPlanTrial;

public final class PagBankPlanMapper {

    private PagBankPlanMapper() {}

    public static PagBankPlan from(Plan plan) {
        return PagBankPlan.builder()
                .name(plan.getName())
                .referenceId(plan.getReferenceId())
                .description(plan.getDescription())
                .amount(mapAmount(plan))
                .setupFee(plan.getSetupFee())
                .limitSubscriptions(plan.getLimitSubscriptions())
                .interval(mapInterval(plan))
                .billingCycles(plan.getBillingCycles())
                .trial(mapTrial(plan))
                .paymentMethod(plan.getPaymentMethods())
                .build();
    }

    public static Plan to(PagBankPlan plan) {
        return Plan.builder()
                .id(plan.getId())
                .name(plan.getName())
                .status(plan.getStatus())
                .referenceId(plan.getReferenceId())
                .description(plan.getDescription())
                .amount(plan.getAmount().value(), plan.getAmount().currency())
                .setupFee(plan.getSetupFee())
                .limitSubscriptions(plan.getLimitSubscriptions())
                .interval(plan.getInterval().length(), plan.getInterval().unit())
                .billingCycles(plan.getBillingCycles())
                .trial(plan.getTrial().days(), plan.getTrial().enabled(), plan.getTrial().holdSetupFee())
                .paymentMethods(plan.getPaymentMethod())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .editable(plan.getEditable())
                .build();
    }

    public static PageResponse<Plan> toPageResponse(PagBankListPlan pagBankListPlan) {
        return new PageResponse<>(
                new PageResponse.PageInfo(
                        pagBankListPlan.getResultSet().offset(),
                        pagBankListPlan.getResultSet().limit(),
                        pagBankListPlan.getResultSet().total()),
                pagBankListPlan.getPlans().stream().map(PagBankPlanMapper::to).toList()
        );
    }


    private static PagBankAmount mapAmount(Plan plan) {

        return new PagBankAmount(
            plan.getAmount().value(),
            plan.getAmount().currency()
        );
    }

    private static PagBankPlanInterval mapInterval(Plan plan) {
        return new PagBankPlanInterval(
            plan.getInterval().length(),
            plan.getInterval().unit()
        );
    }

    private static PagBankPlanTrial mapTrial(Plan plan) {
        if (plan.getTrial() == null) {
            return null;
        }

        return new PagBankPlanTrial(
            plan.getTrial().days(),
            plan.getTrial().enabled(),
            plan.getTrial().hold_setup_fee()
        );
    }
}