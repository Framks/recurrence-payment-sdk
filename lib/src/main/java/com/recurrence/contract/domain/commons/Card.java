package com.recurrence.contract.domain.commons;

/**
 * Cartão usado como meio de pagamento.
 *
 * <p>{@code token} é o cartão gerado pelo SDK de frontend do provedor ativo:
 * no PagBank o cartão criptografado ({@code encrypted}), no Mercado Pago o
 * {@code card_token_id}. {@code securityCode} é o CVV, exigido pelo PagBank na
 * cobrança.</p>
 *
 * <p>Para gerar o token no backend use {@code recurrence.card().tokenize(...)},
 * que já devolve o {@code Card} preenchido para o provedor ativo.</p>
 *
 * <p>Nas respostas, {@code token} e {@code securityCode} vêm vazios e apenas os
 * dados de exibição ({@code brand}, {@code lastDigits}) são preenchidos quando o
 * provedor os devolve.</p>
 *
 * <pre>{@code
 * Card card = Card.builder()
 *     .token("TOKEN_GERADO_NO_FRONTEND")
 *     .securityCode("123")
 *     .build();
 * }</pre>
 */
public final class Card {

    private final String token;
    private final String securityCode;
    private final String brand;
    private final String lastDigits;

    private Card(Builder builder) {
        this.token = builder.token;
        this.securityCode = builder.securityCode;
        this.brand = builder.brand;
        this.lastDigits = builder.lastDigits;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getToken() {
        return token;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public String getBrand() {
        return brand;
    }

    public String getLastDigits() {
        return lastDigits;
    }

    public static final class Builder {
        private String token;
        private String securityCode;
        private String brand;
        private String lastDigits;

        private Builder() { }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder securityCode(String securityCode) {
            this.securityCode = securityCode;
            return this;
        }

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder lastDigits(String lastDigits) {
            this.lastDigits = lastDigits;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }
}
