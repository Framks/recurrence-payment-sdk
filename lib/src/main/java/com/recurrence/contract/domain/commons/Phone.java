package com.recurrence.contract.domain.commons;

/**
 * Telefone de contato do assinante.
 *
 * <pre>{@code
 * Phone phone = Phone.builder()
 *     .country("55")
 *     .area("88")
 *     .number("999999999")
 *     .build();
 * }</pre>
 */
public final class Phone {

    private final Integer id;
    private final String area;
    private final String country;
    private final String number;

    private Phone(Builder builder) {
        this.id = builder.id;
        this.area = builder.area;
        this.country = builder.country;
        this.number = builder.number;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getId() {
        return id;
    }

    public String getArea() {
        return area;
    }

    public String getCountry() {
        return country;
    }

    public String getNumber() {
        return number;
    }

    public static final class Builder {
        private Integer id;
        private String area;
        private String country;
        private String number;

        private Builder() { }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder area(String area) {
            this.area = area;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Phone build() {
            return new Phone(this);
        }
    }
}
