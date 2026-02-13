package com.recurrence.contract.domain.infra;

import java.util.List;
import java.util.Map;

public class RequestResponse<T> {

    private final int httpResponse;
    private final List<Map<String, String>> headers;
    private T body;
    private RequestErrorResponseList errors;

    public RequestResponse(final int httpResponse, final List<Map<String, String>> headers) {
        this.httpResponse = httpResponse;
        this.headers = headers;
    }

    public int getHttpResponse() {
        return httpResponse;
    }

    public List<Map<String, String>> getHeaders() {
        return headers;
    }

    public T getBody() {
        return body;
    }

    public RequestErrorResponseList getErrors() {
        return errors;
    }

    public RequestResponse setBody(T body) {
        this.body = body;
        return this;
    }

    public RequestResponse setErrors(RequestErrorResponseList errors) {
        this.body = null;
        this.errors = errors;
        return this;
    }

    public record RequestErrorResponseList(
            List<RequestErrorResponseDetail> errors) {
    }

    public record RequestErrorResponseDetail(
            String errorKey,
            String message,
            String description) {
    }
}
