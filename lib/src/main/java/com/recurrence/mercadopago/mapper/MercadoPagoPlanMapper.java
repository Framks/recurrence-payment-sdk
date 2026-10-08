package com.recurrence.mercadopago.mapper;

import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.PlanInterval;
import com.recurrence.contract.domain.enums.PaymentMethodType;
import com.recurrence.contract.domain.request.CreatePlanRequest;
import com.recurrence.contract.domain.request.UpdatePlanRequest;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.mercadopago.domain.commons.MercadoPagoAutoRecurring;
import com.recurrence.mercadopago.domain.plan.MercadoPagoPlan;
import com.recurrence.mercadopago.domain.plan.MercadoPagoPlanList;
import com.recurrence.mercadopago.domain.plan.MercadoPagoPlanRequest;

import java.util.List;

/**
 * Plano do contrato x {@code preapproval_plan} do Mercado Pago:
 * {@code name} = {@code reason}; valor, intervalo, ciclos ({@code repetitions})
 * e teste ({@code free_trial}) ficam em {@code auto_recurring}.
 */
public final class MercadoPagoPlanMapper {

    private MercadoPagoPlanMapper() {}

    /**
     * Sem {@code paymentMethods}, restringe a cartão, como no PagBank.
     * {@code back_url} é obrigatório no Mercado Pago e vem da configuração do provedor.
     */
    public static MercadoPagoPlanRequest toRequest(CreatePlanRequest plan, String backUrl) {
        List<PaymentMethodType> paymentMethods = plan.getPaymentMethods() == null
                ? List.of(PaymentMethodType.CREDIT_CARD)
                : plan.getPaymentMethods();
        return new MercadoPagoPlanRequest(
                plan.getName(),
                MercadoPagoAutoRecurring.of(
                        plan.getInterval().frequency(),
                        CommonsMapper.toFrequencyType(plan.getInterval().type()),
                        plan.getBillingCycles(),
                        CommonsMapper.toDecimal(plan.getAmount().value()),
                        CommonsMapper.toCurrencyId(plan.getAmount()),
                        plan.getTrial() == null ? null : CommonsMapper.toFreeTrial(plan.getTrial().days())
                ),
                CommonsMapper.toPaymentMethodsAllowed(paymentMethods),
                backUrl
        );
    }

    /** Atualização parcial: só os campos informados vão no corpo. */
    public static MercadoPagoPlanRequest toRequest(UpdatePlanRequest plan) {
        PlanInterval interval = plan.getInterval();
        Amount amount = plan.getAmount();
        MercadoPagoAutoRecurring autoRecurring = MercadoPagoAutoRecurring.of(
                interval == null ? null : interval.frequency(),
                interval == null ? null : CommonsMapper.toFrequencyType(interval.type()),
                plan.getBillingCycles(),
                amount == null ? null : CommonsMapper.toDecimal(amount.value()),
                CommonsMapper.toCurrencyId(amount),
                plan.getTrial() == null ? null : CommonsMapper.toFreeTrial(plan.getTrial().days())
        );
        return new MercadoPagoPlanRequest(
                plan.getName(),
                autoRecurring.isEmpty() ? null : autoRecurring,
                CommonsMapper.toPaymentMethodsAllowed(plan.getPaymentMethods()),
                null
        );
    }

    public static Plan toContract(MercadoPagoPlan plan) {
        Plan.Builder builder = Plan.builder()
                .id(plan.id())
                .name(plan.reason())
                .status(CommonsMapper.toPlanStatus(plan.status()))
                .paymentMethods(CommonsMapper.toContractPaymentMethods(plan.paymentMethodsAllowed()))
                .createdAt(plan.dateCreated())
                .updatedAt(plan.lastModified());

        MercadoPagoAutoRecurring recurring = plan.autoRecurring();
        if (recurring != null) {
            Amount amount = CommonsMapper.toContract(recurring.transactionAmount(), recurring.currencyId());
            if (amount != null) {
                builder.amount(amount.value(), amount.currency());
            }
            PlanInterval interval = CommonsMapper.toContractInterval(recurring.frequency(), recurring.frequencyType());
            if (interval != null) {
                builder.interval(interval.type(), interval.frequency());
            }
            builder.billingCycles(recurring.repetitions());
            Integer trialDays = CommonsMapper.toTrialDays(recurring.freeTrial());
            if (trialDays != null) {
                builder.trial(trialDays);
            }
        }
        return builder.build();
    }

    public static PageResponse<Plan> toPage(MercadoPagoPlanList list) {
        return CommonsMapper.toPage(list.paging(), list.results(), MercadoPagoPlanMapper::toContract);
    }
}
