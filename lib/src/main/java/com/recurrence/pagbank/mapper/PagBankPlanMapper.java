package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.commons.PlanInterval;
import com.recurrence.contract.domain.commons.PlanTrial;
import com.recurrence.contract.domain.enums.PaymentMethodType;
import com.recurrence.contract.domain.request.CreatePlanRequest;
import com.recurrence.contract.domain.request.UpdatePlanRequest;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.pagbank.domain.plan.PagBankListPlan;
import com.recurrence.pagbank.domain.plan.PagBankPlan;
import com.recurrence.pagbank.domain.plan.PagBankPlanRequest;
import com.recurrence.pagbank.domain.plan.PagBankPlanTrial;

import java.util.List;

public final class PagBankPlanMapper {

    private PagBankPlanMapper() {}

    /** Sem {@code paymentMethods}, envia só cartão para não herdar o padrão do PagBank (que inclui boleto). */
    public static PagBankPlanRequest toRequest(CreatePlanRequest plan) {
        List<PaymentMethodType> paymentMethods = plan.getPaymentMethods() == null
                ? List.of(PaymentMethodType.CREDIT_CARD)
                : plan.getPaymentMethods();
        return new PagBankPlanRequest(
                plan.getName(),
                CommonsMapper.toPagBank(plan.getAmount()),
                CommonsMapper.toPagBank(plan.getInterval()),
                plan.getBillingCycles(),
                toPagBank(plan.getTrial()),
                CommonsMapper.toPagBankPaymentMethods(paymentMethods)
        );
    }

    public static PagBankPlanRequest toRequest(UpdatePlanRequest plan) {
        return new PagBankPlanRequest(
                plan.getName(),
                CommonsMapper.toPagBank(plan.getAmount()),
                CommonsMapper.toPagBank(plan.getInterval()),
                plan.getBillingCycles(),
                toPagBank(plan.getTrial()),
                CommonsMapper.toPagBankPaymentMethods(plan.getPaymentMethods())
        );
    }

    public static Plan toContract(PagBankPlan plan) {
        PlanInterval interval = CommonsMapper.toContract(plan.interval());
        Plan.Builder builder = Plan.builder()
                .id(plan.id())
                .name(plan.name())
                .status(CommonsMapper.toPlanStatus(plan.status()))
                .billingCycles(plan.billingCycles())
                .paymentMethods(CommonsMapper.toContractPaymentMethods(plan.paymentMethod()))
                .createdAt(plan.createdAt())
                .updatedAt(plan.updatedAt());
        if (plan.amount() != null) {
            var amount = CommonsMapper.toContract(plan.amount());
            builder.amount(amount.value(), amount.currency());
        }
        if (interval != null) {
            builder.interval(interval.type(), interval.frequency());
        }
        if (plan.trial() != null && Boolean.TRUE.equals(plan.trial().enabled())) {
            builder.trial(plan.trial().days());
        }
        return builder.build();
    }

    public static PageResponse<Plan> toPage(PagBankListPlan list) {
        return CommonsMapper.toPage(list.resultSet(), list.plans(), PagBankPlanMapper::toContract);
    }

    private static PagBankPlanTrial toPagBank(PlanTrial trial) {
        if (trial == null) return null;
        return new PagBankPlanTrial(trial.days(), true, null);
    }
}