package com.recurrence.contract.domain.request;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.enums.Currency;

import java.util.List;

/**
 * Dados aceitos ao atualizar uma assinatura. Todos os campos são opcionais:
 * um campo nulo significa "não alterar".
 *
 * <p>Interseção entre os provedores:</p>
 * <ul>
 *   <li>{@code planId} &mdash; PagBank {@code plan.id} / Mercado Pago {@code preapproval_plan_id};</li>
 *   <li>{@code amount} &mdash; PagBank {@code amount} / Mercado Pago {@code auto_recurring.transaction_amount};</li>
 *   <li>{@code paymentMethod} &mdash; PagBank {@code /customers/{id}/billing_info} /
 *       Mercado Pago {@code card_token_id} (o adaptador roteia para o endpoint correto).</li>
 * </ul>
 *
 * <p>Mudança de estado (pausar/reativar/cancelar) não entra aqui: use os métodos
 * dedicados de {@code SubscriptionService}.</p>
 */
public final class UpdateSubscriptionRequest {

    private final String planId;
    private final Amount amount;
    private final List<BillingInfo> paymentMethod;

    private UpdateSubscriptionRequest(Builder builder) {
        this.planId = builder.planId;
        this.amount = builder.amount;
        this.paymentMethod = builder.paymentMethod;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPlanId() {
        return planId;
    }

    public Amount getAmount() {
        return amount;
    }

    public List<BillingInfo> getPaymentMethod() {
        return paymentMethod;
    }

    public static final class Builder {
        private String planId;
        private Amount amount;
        private List<BillingInfo> paymentMethod;

        private Builder() { }

        public Builder planId(String planId) {
            this.planId = planId;
            return this;
        }

        public Builder amount(Integer value, Currency currency) {
            this.amount = new Amount(value, currency);
            return this;
        }

        public Builder paymentMethod(List<BillingInfo> paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public UpdateSubscriptionRequest build() {
            return new UpdateSubscriptionRequest(this);
        }
    }
}
