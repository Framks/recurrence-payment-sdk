package com.recurrence.pagbank.client;

import com.recurrence.infrastructure.HttpClientAdapter;

import com.recurrence.pagbank.PagBankEnvironment;
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

    public HttpResponse<String> list(int offset, int limit, final Map<String, String> headers) {
        // ToDo saber se nos outros providers tem alguma estrutura de query para fazera a filtragem, ou ordenação, etc..
        return httpClient.get(uriList(offset, limit), headers);
    }

    public HttpResponse<String> get(final String id, final Map<String, String> headers) {
        return httpClient.get(uriGet(id), headers);
    }

    public HttpResponse<String> update(final String id, final String payload, final Map<String, String> headers) {
        return httpClient.put(uriUpdate(id), payload, headers);
    }

    public HttpResponse<String> inactivate(final String id, final Map<String, String> headers) {
        return httpClient.post(uriInactivate(id), "", headers);
    }

    public HttpResponse<String> activate(final String id, final Map<String, String> headers) {
        return httpClient.post(uriActivate(id), "", headers);
    }


    private String uriCreate() {
        return this.environment.getHost() + RESOURCE;
    }

    private String uriList(int offset, int limit) {
        return this.environment.getHost() + RESOURCE + "?offset=" + offset + "&limit=" + limit;
    }

    private String uriGet(String id) {
        return this.environment.getHost() + RESOURCE + "/" + id;
    }

    private String uriUpdate(String id) {
        return this.environment.getHost() + RESOURCE + "/" + id;
    }

    private String uriInactivate(String id) {
        return this.environment.getHost() + RESOURCE + "/" + id + "/inactivate";
    }

    private String uriActivate(String id) {
        return this.environment.getHost() + RESOURCE + "/" + id + "/activate";
    }
}
