package com.recurrence.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.exception.RecurrenceException;
import com.recurrence.exception.RecurrenceProviderException;
import com.recurrence.exception.RecurrenceSerializationException;
import com.recurrence.exception.RecurrenceValidationException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseService {

    protected Map<String, String> defaultHeaders(String token) {
        return new HashMap<>(Map.of(
                "Authorization", "Bearer " + token,
                "Content-Type", "application/json",
                "Accept", "application/json"
        ));
    }

    protected <T> String toJson(T obj) throws JsonProcessingException {
        return Converter.mapper().writeValueAsString(obj);
    }

    protected <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return Converter.mapper().readValue(json, clazz);
    }

    protected <T> RequestResponse<T> buildResponse(HttpResponse<String> response) {
        return new RequestResponse<>(
                response.statusCode(),
                extractHeaders(response)
        );
    }

    protected <T> T executeProviderOperation(
            String provider,
            String operation,
            ThrowingSupplier<T> supplier
    ) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw providerException(provider, operation, e);
        }
    }

    protected RuntimeException providerException(String provider, String operation, Exception e) {
        if (e instanceof RecurrenceException recurrenceException) {
            return recurrenceException;
        }
        if (e instanceof JsonProcessingException) {
            return new RecurrenceSerializationException(provider + " " + operation, e);
        }
        if (e instanceof IllegalArgumentException) {
            return new RecurrenceValidationException(e.getMessage(), e);
        }
        return new RecurrenceProviderException(provider, operation, e);
    }

    protected int pageValue(Integer value, String fieldName) {
        if (value == null) {
            throw new RecurrenceValidationException(fieldName + " must be provided");
        }
        if (value < 0) {
            throw new RecurrenceValidationException(fieldName + " must be between 0 and " + Integer.MAX_VALUE);
        }
        return value;
    }

    private List<Map<String, String>> extractHeaders(HttpResponse<String> response) {
        return response.headers()
                .map()
                .entrySet()
                .stream()
                .flatMap(entry -> entry.getValue()
                        .stream()
                        .map(value -> Map.of(entry.getKey(), value)))
                .toList();
    }

    @FunctionalInterface
    protected interface ThrowingSupplier<T> {
        T get() throws Exception;
    }
}
