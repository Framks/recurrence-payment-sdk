package com.recurrence.pagbank.coupon;

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

    public HttpResponse<String> list(final Map<String, String> headers) {
        return httpClient.get(uriList(), headers);
    }

    private String uriCreate() {
        return this.environment.getHost() + RESOURCE;
    }

    private String uriList() {
        return this.environment.getHost() + RESOURCE;
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
