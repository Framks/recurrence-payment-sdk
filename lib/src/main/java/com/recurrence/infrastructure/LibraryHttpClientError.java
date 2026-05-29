package com.recurrence.infrastructure;

import com.recurrence.exception.RecurrenceHttpException;

public class LibraryHttpClientError extends RecurrenceHttpException {
    public LibraryHttpClientError(final String url, String message, Throwable e) {
        super(url, message, e);
    }
}
