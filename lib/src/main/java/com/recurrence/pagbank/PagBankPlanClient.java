package com.recurrence.pagbank;

import com.recurrence.infrastructure.HttpClientAdapter;

import java.net.http.HttpResponse;
import java.util.Map;

public class PagBankPlanClient {

    private final HttpClientAdapter httpClient = new HttpClientAdapter();
    private final PagBankEnvironment environment;
    private static final String RESOURCE = "/plans";

    public PagBankPlanClient(PagBankEnvironment environment) {
        this.environment = environment;
    }

    public HttpResponse<String> create(final String payload, final Map<String, String> headers) {
        return httpClient.post(uriCreate(), payload, headers);
    }

    private String uriCreate() {
        return this.environment.getHost() + RESOURCE;
    }
}
