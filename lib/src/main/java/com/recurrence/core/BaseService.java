package com.recurrence.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recurrence.contract.infra.RequestResponse;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public abstract class BaseService {

    protected Map<String, String> defaultHeaders(String token) {
        return Map.of(
                "Authorization", "Bearer " + token,
                "Content-Type", "application/json",
                "Accept", "application/json"
        );
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
}

