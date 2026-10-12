package com.recurrence.contract.filters;

import com.recurrence.contract.domain.enums.PlanStatus;

/**
 * Filtros de busca de planos. Paginação fica em {@code PageOptions}.
 *
 * <p>Interseção fraca entre os provedores &mdash; confirmar na PoC:</p>
 * <ul>
 *   <li>{@code status} &mdash; Mercado Pago {@code status} (busca de {@code preapproval_plan});
 *       PagBank possui {@code status} no objeto plano, mas o filtro na listagem ainda
 *       precisa ser confirmado;</li>
 *   <li>{@code query} &mdash; Mercado Pago {@code q}; PagBank {@code reference_id}
 *       (o adaptador mapeia para o parâmetro suportado).</li>
 * </ul>
 *
 * <p>O adaptador ignora, sem erro, o campo que o provedor ativo não suportar.</p>
 */
public final class PlanFilter {

    private final PlanStatus status;
    private final String query;

    private PlanFilter(Builder builder) {
        this.status = builder.status;
        this.query = builder.query;
    }

    public static Builder builder() {
        return new Builder();
    }

    public PlanStatus getStatus() {
        return status;
    }

    public String getQuery() {
        return query;
    }

    public static final class Builder {
        private PlanStatus status;
        private String query;

        private Builder() { }

        public Builder status(PlanStatus status) {
            this.status = status;
            return this;
        }

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public PlanFilter build() {
            return new PlanFilter(this);
        }
    }
}
