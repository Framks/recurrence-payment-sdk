package com.recurrence.exception;

public class UnsupportedProviderTypeException extends RecurrenceException {
    public UnsupportedProviderTypeException(String message) {
        super(message);
    }
    public UnsupportedProviderTypeException(String provider, String operation) {
        super(provider + " operation failed: " + operation);
    }
}
