package com.recurrence.pagbank.client;

import com.recurrence.infrastructure.HttpClientAdapter;
import com.recurrence.pagbank.PagbankConfig;
import java.net.http.HttpResponse;
import java.util.Map;

public class PagBankCouponClient {

    private final HttpClientAdapter httpClient = new HttpClientAdapter();
    private final PagbankConfig config;
    private static final String RESOURCE = "/coupons";


    // ajustar o client para passar o timeout da configuração.
    // se o config ja vai ter os headers. as funções precisam mesmo receber os headers?
    // como usar o requestOptions nesse caso

    public PagBankCouponClient(final PagbankConfig config) {
        this.config = config;
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
        return this.config.getHost() + RESOURCE;
    }

    private String uriList(int offset, int limit) {
        return this.config.getHost() + RESOURCE + "?offset=" + offset + "&limit=" + limit;
    }

    private String uriGet(String id) {
        return this.config.getHost() + RESOURCE + "/" + id;
    }

    private String uriInactivate(String id) {
        return this.config.getHost() + RESOURCE + "/" + id + "/inactivate";
    }

    private String uriActivate(String id) {
        return this.config.getHost() + RESOURCE + "/" + id + "/activate";
    }
}
