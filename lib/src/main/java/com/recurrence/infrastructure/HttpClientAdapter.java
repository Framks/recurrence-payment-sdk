package com.recurrence.infrastructure;

import com.recurrence.exception.RecurrenceNetworkException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;

/**
 * Envia requisições HTTP. Não interpreta o status: qualquer resposta obtida é
 * devolvida; apenas falhas sem resposta viram {@link RecurrenceNetworkException}.
 *
 * <p>Não é {@code final} para permitir substituição em testes.</p>
 */
public class HttpClientAdapter {

    private final HttpClient httpClient;

    public HttpClientAdapter() {
        this(HttpClient.newHttpClient());
    }

    public HttpClientAdapter(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpResponse<String> send(HttpRequestRecurrence request) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(request.getUrl()));

        request.getHeaders().forEach(requestBuilder::header);

        switch (request.getMethod()) {
            case POST -> requestBuilder.POST(bodyOf(request));
            case PUT -> requestBuilder.PUT(bodyOf(request));
            case DELETE -> requestBuilder.DELETE();
            case GET -> requestBuilder.GET();
        }

        if (request.getTimeout() != null) {
            requestBuilder.timeout(request.getTimeout());
        }

        try {
            return httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (HttpTimeoutException e) {
            throw new RecurrenceNetworkException(request.getUrl(), request.getMethod() + " timed out", e);
        } catch (IOException e) {
            throw new RecurrenceNetworkException(request.getUrl(), request.getMethod() + " failed", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RecurrenceNetworkException(request.getUrl(), request.getMethod() + " interrupted", e);
        }
    }

    private static HttpRequest.BodyPublisher bodyOf(HttpRequestRecurrence request) {
        return request.getBody() == null
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(request.getBody());
    }
}
