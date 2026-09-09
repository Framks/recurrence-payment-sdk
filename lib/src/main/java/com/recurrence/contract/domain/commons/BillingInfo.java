package com.recurrence.contract.domain.commons;

import com.recurrence.contract.domain.enums.PaymentMethodType;

/**
 * Meio de pagamento associado a uma assinatura ou assinante.
 *
 * <p>É uma classe (e não um record) para permitir a inclusão futura de outros
 * meios de pagamento sem quebrar o contrato.</p>
 *
 * <pre>{@code
 * BillingInfo billing = BillingInfo.builder()
 *     .type(PaymentMethodType.CREDIT_CARD)
 *     .card(Card.builder().id("CARD_123").build())
 *     .build();
 * }</pre>
 */
public final class BillingInfo {

    private final PaymentMethodType type;
    private final Card card;

    private BillingInfo(Builder builder) {
        this.type = builder.type;
        this.card = builder.card;
    }

    public static Builder builder() {
        return new Builder();
    }

    public PaymentMethodType getType() {
        return type;
    }

    public Card getCard() {
        return card;
    }

    public static final class Builder {
        private PaymentMethodType type;
        private Card card;

        private Builder() { }

        public Builder type(PaymentMethodType type) {
            this.type = type;
            return this;
        }

        public Builder card(Card card) {
            this.card = card;
            return this;
        }

        public BillingInfo build() {
            return new BillingInfo(this);
        }
    }
}
