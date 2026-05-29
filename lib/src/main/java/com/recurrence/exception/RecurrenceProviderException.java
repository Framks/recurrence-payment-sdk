package com.recurrence.exception;

public class RecurrenceProviderException extends RecurrenceException {

    private final String provider;
    private final String operation;

    public RecurrenceProviderException(String provider, String operation, Throwable cause) {
        super(provider + " operation failed: " + operation, cause);
        this.provider = provider;
        this.operation = operation;
    }

    public String getProvider() {
        return provider;
    }

    public String getOperation() {
        return operation;
    }
}
