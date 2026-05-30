package com.recurrence.infrastructure;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

final public class HttpClientAdapter {

    private final HttpClient httpClient;

    public HttpClientAdapter() {
        this.httpClient = HttpClient.newHttpClient();
    }

    // Criar um singleton para esse cara.

    public HttpResponse<String> post(
        final String url,
        final String body,
        final Map<String, String> headers
    ) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(15))
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(body));

            headers.forEach(requestBuilder::header);

            var request = requestBuilder.build();

            return httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {
            throw new LibraryHttpClientError(url, e.getMessage(), e);
        }
    }

    public HttpResponse<String> get(
        final String url,
        final Map<String, String> headers
    ) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(15))
                .uri(URI.create(url))
                .GET();

            headers.forEach(requestBuilder::header);

            var request = requestBuilder.build();

            return httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {
            throw new LibraryHttpClientError(url, e.getMessage(), e);
        }
    }

    public HttpResponse<String> put(
        final String url,
        final String body,
        final Map<String, String> headers
    ){
        try {
            HttpRequest.BodyPublisher publisher =
                (body == null || body.isBlank())
                    ? HttpRequest.BodyPublishers.noBody()
                    : HttpRequest.BodyPublishers.ofString(body);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(15))
                .uri(URI.create(url))
                .PUT(publisher);

            headers.forEach(requestBuilder::header);

            var request = requestBuilder.build();

            return httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {
            throw new LibraryHttpClientError(url, e.getMessage(), e);
        }
    }

    public HttpResponse<String> delete(
        final String url,
        final Map<String, String> headers
    ){
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(15))
                .uri(URI.create(url))
                .DELETE();

            headers.forEach(requestBuilder::header);

            var request = requestBuilder.build();

            return httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {
            throw new LibraryHttpClientError(url, e.getMessage(), e);
        }
    }
}
