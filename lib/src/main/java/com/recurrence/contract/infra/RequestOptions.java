package com.recurrence.contract.infra;

import java.time.Duration;

public class RequestOptions {
    private final String idempotencyKey;
    private final Duration timeout;

    private RequestOptions(Builder builder) {
        this.idempotencyKey = builder.idempotencyKey;
        this.timeout = builder.timeout;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static RequestOptions empty() {
        return builder().build();
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public static final class Builder {
        private String idempotencyKey;
        private Duration timeout;

        private Builder() {}

        public Builder idempotencyKey(String idempotencyKey) {
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public RequestOptions build() {
            return new RequestOptions(this);
        }
    }
}
