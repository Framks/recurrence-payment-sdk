package com.recurrence.exception;

/**
 * Falha ao converter o payload de/para JSON.
 */
public class RecurrenceSerializationException extends RecurrenceException {

    public RecurrenceSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
