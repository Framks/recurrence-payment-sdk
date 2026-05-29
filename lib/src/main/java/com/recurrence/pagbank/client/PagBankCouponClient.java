package com.recurrence.pagbank.client;

import com.recurrence.infrastructure.HttpClientAdapter;
import com.recurrence.pagbank.PagBankEnvironment;
import java.net.http.HttpResponse;
import java.util.Map;

public class PagBankCouponClient {

    private final HttpClientAdapter httpClient = new HttpClientAdapter();
    private final PagBankEnvironment environment;
    private static final String RESOURCE = "/coupons";

    public PagBankCouponClient(PagBankEnvironment environment) {
        this.environment = environment;
    }

    public HttpResponse<String> create(final String payload, final Map<String, String> headers) {
        return httpClient.post(uriCreate(), payload, headers);
    }

    public HttpResponse<String> list(Integer offset, Integer limit, final Map<String, String> headers) {
        return httpClient.get(uriList(offset, limit), headers);
    }

    public HttpResponse<String> get(final String id, final Map<String, String> headers) {
        return httpClient.get(uriGet(id), headers);
    }

    public HttpResponse<String> inactivate(final String id, final Map<String, String> headers) {
        return httpClient.put(uriInactivate(id), null, headers);
    }

    public HttpResponse<String> activate(final String id, final Map<String, String> headers) {
        return httpClient.put(uriActivate(id), null, headers);
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

    private String uriInactivate(String id) {
        return this.environment.getHost() + RESOURCE + "/" + id + "/inactivate";
    }

    private String uriActivate(String id) {
        return this.environment.getHost() + RESOURCE + "/" + id + "/activate";
    }
}
