package com.recurrence.infrastructure;

import com.recurrence.infrastructure.Exceptions.HttpClientException;
import com.recurrence.utils.Json;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public final class HttpClientAdapter {

    private final HttpClient httpClient;

    public HttpClientAdapter(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpClientAdapter() {
        this(HttpClient.newHttpClient());
    }

    public <T> T post(
            String url,
            Object body,
            Map<String, String> headers,
            Class<T> responseType
    ) {
        try {
            String jsonBody = Json.MAPPER.writeValueAsString(body);

            HttpRequest.Builder builder = HttpRequest.newBuilder() // ToDo devemos adicionar o timeout aqui?
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

            headers.forEach(builder::header);

            HttpResponse<String> response =
                    httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new HttpClientException(
                        response.statusCode(),
                        response.body()
                );
            }

            if (responseType == Void.class) {
                return null;
            }

            return Json.MAPPER.readValue(response.body(), responseType);

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Erro ao executar chamada HTTP", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado", e);
        }
    }

    public <T> T get(
            String url,
            Map<String, String> headers,
            Class<T> responseType
    ) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET();

            headers.forEach(builder::header);

            HttpResponse<String> response =
                    httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new HttpClientException(response.statusCode(), response.body());
            }

            return Json.MAPPER.readValue(response.body(), responseType);

        } catch (Exception e) {
            throw new RuntimeException("Erro GET HTTP", e);
        }
    }

    public <T> T put(
            String url,
            Object body, Map<String, String> headers,
            Class<T> responseType
    ) {
        try {
            String jsonBody = Json.MAPPER.writeValueAsString(body);

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody));

            headers.forEach(builder::header);

            HttpResponse<String> response =
                    httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new HttpClientException(response.statusCode(), response.body());
            }

            if (responseType == Void.class) {
                return null;
            }

            return Json.MAPPER.readValue(response.body(), responseType);

        } catch (Exception e) {
            throw new RuntimeException("Erro PUT HTTP", e);
        }
    }

    public <T> T delete(
            String url,
            Map<String, String> headers,
            Class<T> responseType
    ) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE();

            headers.forEach(builder::header);

            HttpResponse<String> response =
                    httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new HttpClientException(response.statusCode(), response.body());
            }

            return Json.MAPPER.readValue(response.body(), responseType);

        } catch (Exception e) {
            throw new RuntimeException("Erro GET HTTP", e);
        }
    }
}
