package com.recurrence.exception;

import com.recurrence.contract.config.Provider;

import java.util.List;

/**
 * O provedor respondeu com status fora de 2xx.
 *
 * <pre>{@code
 * try {
 *     recurrence.plan().get("PLAN_123");
 * } catch (RecurrenceApiException e) {
 *     if (e.getCategory() == ErrorCategory.NOT_FOUND) { ... }
 * }
 * }</pre>
 */
public class RecurrenceApiException extends RecurrenceException {

    private final int statusCode;
    private final ErrorCategory category;
    private final List<ApiError> errors;
    private final Provider provider;
    private final String operation;
    private final String rawBody;

    public RecurrenceApiException(
            int statusCode,
            Provider provider,
            String operation,
            List<ApiError> errors,
            String rawBody
    ) {
        super(buildMessage(statusCode, provider, operation, errors));
        this.statusCode = statusCode;
        this.category = ErrorCategory.fromStatus(statusCode);
        this.provider = provider;
        this.operation = operation;
        this.errors = errors == null ? List.of() : List.copyOf(errors);
        this.rawBody = rawBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ErrorCategory getCategory() {
        return category;
    }

    public List<ApiError> getErrors() {
        return errors;
    }

    public Provider getProvider() {
        return provider;
    }

    public String getOperation() {
        return operation;
    }

    /** Corpo original da resposta, para diagnóstico. */
    public String getRawBody() {
        return rawBody;
    }

    private static String buildMessage(int statusCode, Provider provider, String operation, List<ApiError> errors) {
        String detail = errors == null || errors.isEmpty() || errors.get(0).message() == null
                ? ""
                : ": " + errors.get(0).message();
        return provider + " " + operation + " failed with HTTP " + statusCode + detail;
    }
}
