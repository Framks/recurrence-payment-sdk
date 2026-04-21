package com.recurrence.pagbank.domain.commons;

public class PagBankPreferencesNotification {
    private String url;
    private EmailPreferences email;

    public PagBankPreferencesNotification() {}

    private PagBankPreferencesNotification(Builder builder) {
        this.url = builder.webhook;
        this.email = new EmailPreferences(
            new Merchant(builder.emailMerchant),
            new Customer(builder.emailCustomer)
        );
    }

    public record EmailPreferences(
        Merchant merchant,
        Customer customer
    ) {}
    public record Merchant(
        Boolean enabled
    ) {}
    public record Customer(
        Boolean enabled
    ) {}

    public String getUrl() {
        return url;
    }
    public EmailPreferences getEmail() {
        return email;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public void setEmail(EmailPreferences email) {
        this.email = email;
    }

    public static Builder builder(){
        return new Builder();
    }

    // Builder
    public static class Builder {
        private Boolean emailMerchant;
        private Boolean emailCustomer;
        private String webhook;

        public Builder emailMerchant(Boolean emailMerchant) {
            this.emailMerchant = emailMerchant;
            return this;
        }

        public Builder emailCustomer(Boolean emailCustomer) {
            this.emailCustomer = emailCustomer;
            return this;
        }

        public Builder webhook(String webhook) {
            this.webhook = webhook;
            return this;
        }

        public PagBankPreferencesNotification build() {
            return new PagBankPreferencesNotification(this);
        }
    }
}
