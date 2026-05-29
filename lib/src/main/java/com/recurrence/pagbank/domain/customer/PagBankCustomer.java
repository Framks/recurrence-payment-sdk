package com.recurrence.pagbank.domain.customer;

import com.recurrence.pagbank.domain.commons.Address;
import com.recurrence.pagbank.domain.commons.PagBankBillingInfo;
import com.recurrence.pagbank.domain.commons.Phone;
import java.time.OffsetDateTime;
import java.util.List;

public class PagBankCustomer {

    private String id;
    private String referenceId;
    private String name;
    private String email;
    private String taxId;
    private String birthDate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<Phone> phones;
    private Address address;
    private List<PagBankBillingInfo> pagBankBillingInfo;

    public PagBankCustomer() {}

    private PagBankCustomer(PagBankCustomer.Builder builder) {
        this.id = builder.id;
        this.referenceId = builder.referenceId;
        this.name = builder.name;
        this.email = builder.email;
        this.taxId = builder.taxId;
        this.birthDate = builder.birthDate;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.phones = builder.phone;
        this.address = builder.address;
        this.pagBankBillingInfo = builder.pagBankBillingInfo;
    }

    public static PagBankCustomer.Builder builder() {
        return new PagBankCustomer.Builder();
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

    public List<Phone> getPhones() {
        return phones;
    }

    public Address getAddress() {
        return address;
    }

    public List<PagBankBillingInfo> getBillingInfo() {
        return pagBankBillingInfo;
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

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setBillingInfo(List<PagBankBillingInfo> pagBankBillingInfo) {
        this.pagBankBillingInfo = pagBankBillingInfo;
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
        private List<PagBankBillingInfo> pagBankBillingInfo;

        public PagBankCustomer.Builder id(String id) {
            this.id = id;
            return this;
        }

        public PagBankCustomer.Builder referenceId(String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public PagBankCustomer.Builder name(String name) {
            this.name = name;
            return this;
        }

        public PagBankCustomer.Builder email(String email) {
            this.email = email;
            return this;
        }

        public PagBankCustomer.Builder taxId(String taxId) {
            this.taxId = taxId;
            return this;
        }

        public PagBankCustomer.Builder birthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public PagBankCustomer.Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PagBankCustomer.Builder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PagBankCustomer.Builder phone(List<Phone> phone) {
            this.phone = phone;
            return this;
        }

        public PagBankCustomer.Builder address(Address address) {
            this.address = address;
            return this;
        }

        public PagBankCustomer.Builder billingInfo(List<PagBankBillingInfo> pagBankBillingInfo) {
            this.pagBankBillingInfo = pagBankBillingInfo;
            return this;
        }

        public PagBankCustomer build() {
            validateBuild();
            return new PagBankCustomer(this);
        }

        // validar ussar isso ou não, pois poderiamos aproveitar essa classe e usar no update,
        // e se a gente for usar o validador pode ficar nas classes de mappers ou criar uma
        // classe validations com as validações para o pags e etc.
        private void validateBuild(){
            if (name == null || name.isBlank()) {
                throw new IllegalStateException("Customer name is required");
            }

            if (email == null || email.isBlank()) {
                throw new IllegalStateException("Customer email is required");
            }

            if (taxId == null || taxId.isBlank()) {
                throw new IllegalStateException("Customer taxId is required");
            }

            if (phone == null || phone.isEmpty()) {
                throw new IllegalStateException("Customer must have at least one phone");
            }
        }

        public PagBankCustomer buildUpdate(){
            return new PagBankCustomer(this);
        }
    }
}
