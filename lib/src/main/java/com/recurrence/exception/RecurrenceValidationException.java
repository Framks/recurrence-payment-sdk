package com.recurrence.exception;

public class RecurrenceValidationException extends RecurrenceException {

    public RecurrenceValidationException(String message) {
        super(message);
    }

    public RecurrenceValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
