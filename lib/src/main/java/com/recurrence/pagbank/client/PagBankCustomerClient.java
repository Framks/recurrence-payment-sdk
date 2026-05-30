package com.recurrence.pagbank.client;

import com.recurrence.infrastructure.HttpClientAdapter;
import com.recurrence.pagbank.PagbankConfig;
import java.net.http.HttpResponse;
import java.util.Map;

public class PagBankCustomerClient {

    private final HttpClientAdapter httpClient = new HttpClientAdapter();
    private final PagbankConfig config;
    private static final String RESOURCE = "/customers";

    public PagBankCustomerClient(PagbankConfig config) {
        this.config = config;
    }

    public HttpResponse<String> create(final String payload, final Map<String, String> headers) {
        return httpClient.post(uriCreate(), payload, headers);
    }

    public HttpResponse<String> list(int offset, int limit, final Map<String, String> headers) {
        return httpClient.get(uriList(offset, limit), headers);
    }

    public HttpResponse<String> get(final String id, final Map<String, String> headers) {
        return httpClient.get(uriGet(id), headers);
    }

    public HttpResponse<String> put(final String id, final String payload, final Map<String, String> headers) {
        return httpClient.put(uriGet(id), payload, headers);
    }

    public HttpResponse<String> putBillingInfo(final String id, final String payload, final Map<String, String> headers) {
        return httpClient.put(uriPutBillingInfo(id), payload, headers);

    }

    private String uriCreate() {
        return this.config.getHost() + RESOURCE;
    }

    private String uriList(int offset, int limit) {
        return this.config.getHost() + RESOURCE + "?offset=" + offset + "&limit=" + limit;
    }

    private String uriGet(String id) {
        return this.config.getHost() + RESOURCE + "/" + id;
    }

    private String uriPutBillingInfo(String id) {
        return this.config.getHost() + RESOURCE + "/" + id + "/billing_info";
    }
}
