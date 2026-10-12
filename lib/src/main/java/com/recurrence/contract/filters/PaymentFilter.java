package com.recurrence.contract.filters;

import com.recurrence.contract.domain.enums.PaymentStatus;

import java.time.OffsetDateTime;

/**
 * Filtros de busca de pagamentos. Paginação fica em {@code PageOptions}.
 *
 * <p>Campos aceitos pelos dois provedores (PagBank {@code /payments},
 * Mercado Pago busca de {@code authorized_payments}):</p>
 * <ul>
 *   <li>{@code status} &mdash; PagBank {@code status} / Mercado Pago {@code status};</li>
 *   <li>{@code createdFrom} / {@code createdUntil} &mdash; PagBank
 *       {@code created_at_start}/{@code created_at_end} / Mercado Pago
 *       {@code date_created.from}/{@code date_created.to}.</li>
 * </ul>
 *
 * <p>{@code payment_method_type} ficou de fora: só o PagBank o expõe na busca.</p>
 */
public final class PaymentFilter {

    private final PaymentStatus status;
    private final OffsetDateTime createdFrom;
    private final OffsetDateTime createdUntil;

    private PaymentFilter(Builder builder) {
        this.status = builder.status;
        this.createdFrom = builder.createdFrom;
        this.createdUntil = builder.createdUntil;
    }

    public static Builder builder() {
        return new Builder();
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedFrom() {
        return createdFrom;
    }

    public OffsetDateTime getCreatedUntil() {
        return createdUntil;
    }

    public static final class Builder {
        private PaymentStatus status;
        private OffsetDateTime createdFrom;
        private OffsetDateTime createdUntil;

        private Builder() { }

        public Builder status(PaymentStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdFrom(OffsetDateTime createdFrom) {
            this.createdFrom = createdFrom;
            return this;
        }

        public Builder createdUntil(OffsetDateTime createdUntil) {
            this.createdUntil = createdUntil;
            return this;
        }

        public PaymentFilter build() {
            return new PaymentFilter(this);
        }
    }
}
