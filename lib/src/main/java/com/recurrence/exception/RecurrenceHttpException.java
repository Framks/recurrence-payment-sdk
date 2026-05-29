package com.recurrence.exception;

public class RecurrenceHttpException extends RecurrenceException {

    private final String url;

    public RecurrenceHttpException(String url, String message, Throwable cause) {
        super("HTTP request failed. URL: " + url + ". Message: " + message, cause);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
