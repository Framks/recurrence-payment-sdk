package com.recurrence.infrastructure;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestRecurrence {

    private Duration timeout;
    private String url;
    private MethodRequest method;
    private String body;
    private Map<String, String> headers;

    private HttpRequestRecurrence() {
    }

    public Duration getTimeout() {
        return timeout;
    }

    public String getUrl() {
        return url;
    }

    public MethodRequest getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Duration timeout;
        private String url;
        private MethodRequest method;
        private String body;
        private Map<String, String> headers = new HashMap<>();

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder method(MethodRequest method) {
            this.method = method;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = new HashMap<>(headers);
            return this;
        }

        public HttpRequestRecurrence build() {
            HttpRequestRecurrence request = new HttpRequestRecurrence();

            request.timeout = this.timeout;
            request.url = this.url;
            request.method = this.method;
            request.body = this.body;
            request.headers = Map.copyOf(this.headers);

            return request;
        }
    }
}
