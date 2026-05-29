package com.recurrence.exception;

public class RecurrenceException extends RuntimeException {

    public RecurrenceException(String message) {
        super(message);
    }

    public RecurrenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
