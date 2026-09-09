package com.recurrence.contract.filters;

import com.recurrence.contract.domain.enums.InvoiceStatus;

/**
 * Filtros de busca de faturas de uma assinatura. Paginação fica em {@code PageOptions}.
 *
 * <p>A listagem de faturas só existe com escopo de assinatura (PagBank
 * {@code /subscriptions/{id}/invoices}, Mercado Pago {@code /authorized_payments/search?preapproval_id=}).
 * O único filtro comum aos dois é {@code status}.</p>
 */
public final class InvoiceFilter {

    private final InvoiceStatus status;

    private InvoiceFilter(Builder builder) {
        this.status = builder.status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public static final class Builder {
        private InvoiceStatus status;

        private Builder() { }

        public Builder status(InvoiceStatus status) {
            this.status = status;
            return this;
        }

        public InvoiceFilter build() {
            return new InvoiceFilter(this);
        }
    }
}
