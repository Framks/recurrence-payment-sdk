package com.recurrence.contract.filters;

import com.recurrence.contract.domain.enums.SubscriptionStatus;

/**
 * Filtros de busca de assinaturas. Paginação fica em {@code PageOptions}.
 *
 * <p>Só campos aceitos pelos dois provedores:</p>
 * <ul>
 *   <li>{@code status} &mdash; PagBank {@code status} / Mercado Pago {@code status};</li>
 *   <li>{@code query} &mdash; PagBank {@code q} / Mercado Pago {@code q}
 *       (nome do assinante, e-mail ou id da assinatura);</li>
 *   <li>{@code referenceId} &mdash; PagBank {@code reference_id} / Mercado Pago {@code external_reference}.</li>
 * </ul>
 *
 * <p>Removidos por não haver equivalência: {@code payment_method_type} (só PagBank)
 * e intervalo de {@code created_at} (a busca de {@code preapproval} do Mercado Pago
 * não expõe filtro por data).</p>
 */
public final class SubscriptionFilter {

    private final SubscriptionStatus status;
    private final String query; // devemos fazer uma validação para não permitir coisas diefrentes dos esperados.
    private final String referenceId;

    private SubscriptionFilter(Builder builder) {
        this.status = builder.status;
        this.query = builder.query;
        this.referenceId = builder.referenceId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public String getQuery() {
        return query;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public static final class Builder {
        private SubscriptionStatus status;
        private String query;
        private String referenceId;

        private Builder() { }

        public Builder status(SubscriptionStatus status) {
            this.status = status;
            return this;
        }

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder referenceId(String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public SubscriptionFilter build() {
            return new SubscriptionFilter(this);
        }
    }
}
