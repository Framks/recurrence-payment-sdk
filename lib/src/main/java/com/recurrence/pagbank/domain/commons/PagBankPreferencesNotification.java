package com.recurrence.pagbank.domain.commons;

import java.util.List;

public class PagBankPreferencesNotification {
    private List<String> urls;
    private EmailPreferences email;

    public PagBankPreferencesNotification() {}
    private PagBankPreferencesNotification(Builder builder) {
        this.urls = builder.webhook;
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

    public List<String> getUrls() {
        return urls;
    }
    public EmailPreferences getEmail() {
        return email;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
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
        private List<String> webhook;

        public Builder emailMerchant(Boolean emailMerchant) {
            this.emailMerchant = emailMerchant;
            return this;
        }

        public Builder emailCustomer(Boolean emailCustomer) {
            this.emailCustomer = emailCustomer;
            return this;
        }

        public Builder webhooks(List<String> webhook) {
            this.webhook = webhook;
            return this;
        }

        public PagBankPreferencesNotification build() {
            return new PagBankPreferencesNotification(this);
        }
    }
}
