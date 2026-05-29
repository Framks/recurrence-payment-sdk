package com.recurrence.contract.domain;

import com.recurrence.contract.domain.commons.Address;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.Phone;
import java.time.OffsetDateTime;
import java.util.List;

// se no momento de fazer um update a gente for usar o mesmo objeto,
// acredito que validar os campos obrigatórios é melhor ficar dentro de cada mapper,
// ou a gente criar outro meio de fazer o update, e nessa abordagem a validação fica só no momento de criar o objeto,
public class Customer {

    private String id;
    private String referenceId;
    private String name;
    private String email;
    private String taxId;              // Todo deixar bem especifico para cada provedor como receber esses dados.
    private String birthDate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<Phone> phone;
    private Address address;
    private BillingInfo billingInfo;

    public Customer() {}

    private Customer(Builder builder) {
        this.id = builder.id;
        this.referenceId = builder.referenceId;
        this.name = builder.name;
        this.email = builder.email;
        this.taxId = builder.taxId;
        this.birthDate = builder.birthDate;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.phone = builder.phone;
        this.address = builder.address;
        this.billingInfo = builder.billingInfo;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getReferenceId() {
        return referenceId;
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

    public String getBirthDate() {
        return birthDate;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Phone> getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public BillingInfo getBillingInfo() {
        return billingInfo;
    }

    // Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setPhone(List<Phone> phone) {
        this.phone = phone;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setBillingInfo(BillingInfo billingInfo) {
        this.billingInfo = billingInfo;
    }

    // Builder
    public static class Builder {

        private String id;
        private String referenceId;
        private String name;
        private String email;
        private String taxId;
        private String birthDate;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private List<Phone> phone;
        private Address address;
        private BillingInfo billingInfo;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder referenceId(String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

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

        public Builder birthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder phone(List<Phone> phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Builder billingInfo(BillingInfo billingInfo) {
            this.billingInfo = billingInfo;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
