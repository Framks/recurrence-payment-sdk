package com.recurrence.exception;

public class RecurrenceSerializationException extends RecurrenceException {

    private final String operation;

    public RecurrenceSerializationException(String operation, Throwable cause) {
        super("Could not serialize or deserialize payload for operation: " + operation, cause);
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
