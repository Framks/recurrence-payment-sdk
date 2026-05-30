package com.recurrence.pagbank.client;

import com.recurrence.infrastructure.HttpClientAdapter;

import com.recurrence.pagbank.PagbankConfig;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Set;

public class PagBankPlanClient {

    private final HttpClientAdapter httpClient = new HttpClientAdapter();
    private final PagbankConfig config;
    private static final String RESOURCE = "/plans";

    public PagBankPlanClient(final PagbankConfig config){
        this.config = config;
    }

    public HttpResponse<String> create(final String payload, final Map<String, String> headers) {
        return httpClient.post(uriCreate(), payload, headers);
    }

    public HttpResponse<String> list(Integer offset, Integer limit, final Map<String, String> headers) {
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
        return this.config.getHost() + RESOURCE;
    }

    private String uriList(Integer offset, Integer limit) {
        return this.config.getHost() + RESOURCE + "?offset=" + offset + "&limit=" + limit;
    }

    private String uriGet(String id) {
        return this.config.getHost() + RESOURCE + "/" + id;
    }

    private String uriUpdate(String id) {
        return this.config.getHost() + RESOURCE + "/" + id;
    }

    private String uriInactivate(String id) {
        return this.config.getHost() + RESOURCE + "/" + id + "/inactivate";
    }

    private String uriActivate(String id) {
        return this.config.getHost() + RESOURCE + "/" + id + "/activate";
    }
}
