package com.recurrence.contract.domain.commons;

/**
 * Cartão usado como meio de pagamento.
 *
 * <p>{@code id} referencia um cartão já tokenizado/salvo no provedor.
 * {@code encrypted} carrega o cartão criptografado no formato exigido pelo
 * provedor e {@code securityCode} é o CVV, quando o fluxo de criação o exigir.</p>
 *
 * <pre>{@code
 * Card card = Card.builder()
 *     .id("CARD_123")
 *     .securityCode("123")
 *     .build();
 * }</pre>
 */
public final class Card {

    private final String id;
    private final String encrypted;
    private final String securityCode;

    private Card(Builder builder) {
        this.id = builder.id;
        this.encrypted = builder.encrypted;
        this.securityCode = builder.securityCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public static final class Builder {
        private String id;
        private String encrypted;
        private String securityCode;

        private Builder() { }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder encrypted(String encrypted) {
            this.encrypted = encrypted;
            return this;
        }

        public Builder securityCode(String securityCode) {
            this.securityCode = securityCode;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }
}
