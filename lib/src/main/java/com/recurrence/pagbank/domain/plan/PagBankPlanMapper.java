package com.recurrence.pagbank.domain.plan;

import com.recurrence.contract.domain.Plan;
import com.recurrence.pagbank.domain.commons.Amount;

public final class PagBankPlanMapper {

    private PagBankPlanMapper() {
    }

    public static PagBankPlan from(final Plan plan) {
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
                .referenceId(plan.getReferenceId())
                .description(plan.getDescription())
                .amount(plan.getAmount().value(), plan.getAmount().currency())
                .setupFee(plan.getSetupFee())
                .limitSubscriptions(plan.getLimitSubscriptions())
                .interval(plan.getInterval().length(), plan.getInterval().unit())
                .billingCycles(plan.getBillingCycles())
                .trial(plan.getTrial().days(), plan.getTrial().enabled(), plan.getTrial().hold_setup_fee())
                .paymentMethods(plan.getPaymentMethod())
                .build();
    }

    private static Amount mapAmount(Plan plan) {
        return new Amount(
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
