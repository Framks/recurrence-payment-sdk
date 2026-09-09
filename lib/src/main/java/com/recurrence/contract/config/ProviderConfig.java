package com.recurrence.contract.config;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

/**
 * Configuration used to initialize a payment provider.
 *
 * <p>Use this class to define authentication token, timeout and default headers
 * used in HTTP requests.</p>
 *
 * <pre>{@code
 * ProviderConfig config = ProviderConfig.builder()
 *     .token("your-token")
 *     .timeout(Duration.ofSeconds(15))
 *     .defaultHeader("Accept", "application/json")
 *     .build();
 * }</pre>
 */
public class ProviderConfig {

    private final Duration timeout;
    private final String token;
    private final Map<String, String> defaultHeaders;
    private final String backUrl; // isso vai servir para o mercado pago.

    private ProviderConfig(Builder build){
        this.token = build.token;
        this.timeout = build.timeout == null ? Duration.ofSeconds(15) : build.timeout;
        this.defaultHeaders = build.defaultHeaders == null ? defaultHeaders() : build.defaultHeaders;
        this.backUrl = build.backUrl;
    }

    private Map<String, String> defaultHeaders(){
        return Map.of(
                "Content-Type", "application/json",
                "Accept", "application/json"
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public Duration getTimeout() {
        return timeout;
    }

    public String getToken() {
        return token;
    }

    public Map<String, String> getDefaultHeaders() {
        return defaultHeaders;
    }

    public String getBackUrl(){
        return backUrl;
    }

    public static class Builder {
        private Duration timeout;
        private String token;
        private Map<String, String> defaultHeaders;
        private String backUrl;

        private Builder() {
        }

        public Builder timeout(final Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Defines the authentication token used by the provider.
         *
         * @param token provider authentication token
         * @return the current builder instance
         */
        public Builder token(final String token) {
            this.token = token;
            return this;
        }

        public Builder defaultHeaders(final Map<String, String> defaultHeaders) {
            this.defaultHeaders = defaultHeaders;
            return this;
        }

        public Builder setBackUrl(final String backUrl){
            this.backUrl = backUrl;
            return this;
        }

        public ProviderConfig build() {
            Objects.requireNonNull(token, "token must not be null");
            return new ProviderConfig(this);
        }
    }
}
