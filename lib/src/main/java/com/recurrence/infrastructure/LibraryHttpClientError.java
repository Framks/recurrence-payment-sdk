package com.recurrence.infrastructure;

public class LibraryHttpClientError extends RuntimeException {
    public LibraryHttpClientError(final String url, String message, Throwable e) {
        super("URL: " + url + "Message: " + message, e);
    }
}
