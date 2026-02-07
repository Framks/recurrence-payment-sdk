package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class HttpClientAdapter {

    private final HttpClient httpClient;

    public HttpClientAdapter() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public HttpResponse<String> post(
            String url,
            String body,
            Map<String, String> headers
    ) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(body));

            headers.forEach(requestBuilder::header);

            HttpRequest request = requestBuilder.build();

            return httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao executar chamada HTTP", e);
        }
    }
}

