package com.recurrence.contract.filters;

import com.recurrence.contract.domain.enums.PlanStatus;

/**
 * Filtros de busca de planos. Paginação fica em {@code PageOptions}.
 *
 * <p>Campos aceitos pelos dois provedores:</p>
 * <ul>
 *   <li>{@code status} &mdash; PagBank {@code status} ({@code ACTIVE} ou {@code INACTIVE}) /
 *       Mercado Pago {@code status} ({@code INACTIVE} é enviado como {@code cancelled});</li>
 *   <li>{@code query} &mdash; PagBank {@code reference_id} / Mercado Pago {@code q}.</li>
 * </ul>
 *
 * <p>O status {@code UNKNOWN} é recusado com {@code RecurrenceValidationException}.</p>
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
