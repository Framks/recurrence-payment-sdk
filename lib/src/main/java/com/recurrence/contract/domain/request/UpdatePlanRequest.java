package com.recurrence.contract.domain.request;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.PlanInterval;
import com.recurrence.contract.domain.commons.PlanTrial;
import com.recurrence.contract.domain.enums.Currency;
import com.recurrence.contract.domain.enums.PaymentMethodType;
import com.recurrence.contract.domain.enums.PlanIntervalType;

import java.util.List;

/**
 * Dados aceitos ao atualizar um plano. Todos os campos são opcionais:
 * um campo nulo significa "não alterar".
 *
 * <p>Interseção entre os provedores (PagBank {@code PUT /plans/{id}} x
 * Mercado Pago {@code PUT /preapproval_plan/{id}}):</p>
 * <ul>
 *   <li>{@code name} &mdash; PagBank {@code name} / Mercado Pago {@code reason};</li>
 *   <li>{@code amount} &mdash; PagBank {@code amount} / Mercado Pago {@code auto_recurring.transaction_amount};</li>
 *   <li>{@code interval} &mdash; PagBank {@code interval} / Mercado Pago {@code auto_recurring.frequency(_type)};</li>
 *   <li>{@code billingCycles} &mdash; PagBank {@code billing_cycles} / Mercado Pago {@code auto_recurring.repetitions};</li>
 *   <li>{@code trial} &mdash; PagBank {@code trial.days} / Mercado Pago {@code auto_recurring.free_trial} (normalizado em dias);</li>
 *   <li>{@code paymentMethods} &mdash; PagBank {@code payment_method} / Mercado Pago {@code payment_methods_allowed}.</li>
 * </ul>
 *
 * <p>Ativar/inativar plano não entra aqui: use {@code PlanService.activate/deactivate}.</p>
 */
public final class UpdatePlanRequest {

    private final String name;
    private final Amount amount;
    private final PlanInterval interval;
    private final Integer billingCycles;
    private final PlanTrial trial;
    private final List<PaymentMethodType> paymentMethods;

    private UpdatePlanRequest(Builder builder) {
        this.name = builder.name;
        this.amount = builder.amount;
        this.interval = builder.interval;
        this.billingCycles = builder.billingCycles;
        this.trial = builder.trial;
        this.paymentMethods = builder.paymentMethods;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public Amount getAmount() {
        return amount;
    }

    public PlanInterval getInterval() {
        return interval;
    }

    public Integer getBillingCycles() {
        return billingCycles;
    }

    public PlanTrial getTrial() {
        return trial;
    }

    public List<PaymentMethodType> getPaymentMethods() {
        return paymentMethods;
    }

    public static final class Builder {
        private String name;
        private Amount amount;
        private PlanInterval interval;
        private Integer billingCycles;
        private PlanTrial trial;
        private List<PaymentMethodType> paymentMethods;

        private Builder() { }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder amount(Integer value, Currency currency) {
            this.amount = new Amount(value, currency);
            return this;
        }

        public Builder interval(PlanIntervalType type, Integer frequency) {
            this.interval = new PlanInterval(type, frequency);
            return this;
        }

        public Builder billingCycles(Integer billingCycles) {
            this.billingCycles = billingCycles;
            return this;
        }

        public Builder trial(Integer days) {
            this.trial = new PlanTrial(days);
            return this;
        }

        public Builder paymentMethods(List<PaymentMethodType> paymentMethods) {
            this.paymentMethods = paymentMethods;
            return this;
        }

        public UpdatePlanRequest build() {
            return new UpdatePlanRequest(this);
        }
    }
}
