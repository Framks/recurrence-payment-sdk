package com.recurrence.infrastructure.Exceptions;

import com.recurrence.exception.RecurrenceException;

public class HttpClientException extends RecurrenceException {
    private final int statusCode;
    private final String responseBody;

    public HttpClientException(int statusCode, String responseBody) {
        super("Erro HTTP: " + statusCode);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
