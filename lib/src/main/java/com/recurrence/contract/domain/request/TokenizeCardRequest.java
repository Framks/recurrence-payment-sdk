package com.recurrence.contract.domain.request;

/**
 * Dados abertos do cartão para {@code card().tokenize(...)}.
 *
 * <p>{@code holderTaxId} (CPF ou CNPJ do titular) é exigido pelo Mercado Pago e
 * ignorado pelo PagBank, que não o inclui no cartão criptografado.</p>
 *
 * <pre>{@code
 * TokenizeCardRequest card = TokenizeCardRequest.builder()
 *     .number("5555666677778884")
 *     .securityCode("123")
 *     .expMonth(12)
 *     .expYear(2030)
 *     .holderName("Joao Pereira Azul")
 *     .holderTaxId("12345678909")
 *     .build();
 * }</pre>
 */
public final class TokenizeCardRequest {

    private final String number;
    private final String securityCode;
    private final Integer expMonth;
    private final Integer expYear;
    private final String holderName;
    private final String holderTaxId;

    private TokenizeCardRequest(Builder builder) {
        this.number = builder.number;
        this.securityCode = builder.securityCode;
        this.expMonth = builder.expMonth;
        this.expYear = builder.expYear;
        this.holderName = builder.holderName;
        this.holderTaxId = builder.holderTaxId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getNumber() {
        return number;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getHolderTaxId() {
        return holderTaxId;
    }

    /** Número e CVV mascarados para não vazarem em logs. */
    @Override
    public String toString() {
        return "TokenizeCardRequest{number=" + mask(number)
                + ", securityCode=***"
                + ", expMonth=" + expMonth
                + ", expYear=" + expYear
                + ", holderName=" + holderName + "}";
    }

    private static String mask(String number) {
        if (number == null || number.length() < 4) return "****";
        return "****" + number.substring(number.length() - 4);
    }

    public static final class Builder {
        private String number;
        private String securityCode;
        private Integer expMonth;
        private Integer expYear;
        private String holderName;
        private String holderTaxId;

        private Builder() { }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Builder securityCode(String securityCode) {
            this.securityCode = securityCode;
            return this;
        }

        /** Mês de validade, de 1 a 12. */
        public Builder expMonth(Integer expMonth) {
            this.expMonth = expMonth;
            return this;
        }

        /** Ano de validade com 4 dígitos. */
        public Builder expYear(Integer expYear) {
            this.expYear = expYear;
            return this;
        }

        public Builder holderName(String holderName) {
            this.holderName = holderName;
            return this;
        }

        /** CPF (11 dígitos) ou CNPJ (14 dígitos) do titular, somente números. */
        public Builder holderTaxId(String holderTaxId) {
            this.holderTaxId = holderTaxId;
            return this;
        }

        public TokenizeCardRequest build() {
            return new TokenizeCardRequest(this);
        }
    }
}
