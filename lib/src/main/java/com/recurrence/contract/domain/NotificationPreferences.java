package com.recurrence.contract.domain;

public class NotificationPreferences {
    private Boolean emailMerchant;
    private Boolean emailCustomer;
    private String webhook;

    private NotificationPreferences(Builder builder) {
        this.emailMerchant = builder.emailMerchant;
        this.emailCustomer = builder.emailCustomer;
        this.webhook = builder.webhook;
    }

    public NotificationPreferences() {}

    public Boolean getEmailMerchant() {
        return emailMerchant;
    }

    public Boolean getEmailCustomer() {
        return emailCustomer;
    }

    public String getWebhook() {
        return webhook;
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

        public NotificationPreferences build() {
            return new NotificationPreferences(this);
        }
    }
}
