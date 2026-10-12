package com.recurrence.contract.domain.request;

import com.recurrence.contract.domain.commons.Phone;

/**
 * Dados do assinante enviados na criação de uma assinatura.
 *
 * <p>O meio de pagamento não fica aqui: use
 * {@link CreateSubscriptionRequest.Builder#paymentMethod}.</p>
 *
 * <pre>{@code
 * CustomerRequest customer = CustomerRequest.builder()
 *     .name("Fulano de Tal")
 *     .email("assinante@exemplo.com")
 *     .taxId("12345678909")
 *     .phone(Phone.builder().country("55").area("88").number("999999999").build())
 *     .build();
 * }</pre>
 */
public final class CustomerRequest {

    private final String name;
    private final String email;
    private final String taxId;
    private final Phone phone;

    private CustomerRequest(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.taxId = builder.taxId;
        this.phone = builder.phone;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTaxId() {
        return taxId;
    }

    public Phone getPhone() {
        return phone;
    }

    public static final class Builder {
        private String name;
        private String email;
        private String taxId;
        private Phone phone;

        private Builder() { }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder taxId(String taxId) {
            this.taxId = taxId;
            return this;
        }

        public Builder phone(Phone phone) {
            this.phone = phone;
            return this;
        }

        public CustomerRequest build() {
            return new CustomerRequest(this);
        }
    }
}
