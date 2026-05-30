package com.recurrence.pagbank.client;

import com.recurrence.infrastructure.HttpClientAdapter;
import com.recurrence.pagbank.PagbankConfig;
import java.net.http.HttpResponse;
import java.util.Map;

public class PagBankPreferencesClient {

    private final HttpClientAdapter httpClient = new HttpClientAdapter();
    private final PagbankConfig config;
    private static final String RESOURCE_KEY = "/public-keys";
    private static final String RESOURCE_NOTIFICATION = "/preferences/notifications";
    private static final String RESOURCE_RETRY = "/preferences/retries";

    public PagBankPreferencesClient(PagbankConfig config) {
        this.config = config;
    }

    public HttpResponse<String> getKey(final Map<String, String> headers) {
        return httpClient.get(uriKey(), headers);
    }

    public HttpResponse<String> putKeys(final Map<String, String> headers) {
        return httpClient.put(uriKey(), "", headers);
    }

    public HttpResponse<String> getNotifications(final Map<String, String> headers){
        return httpClient.get(uriNotification(), headers);
    }

    public HttpResponse<String> putNotification(String payload, final Map<String, String> headers){
        return httpClient.put(uriNotification(), payload, headers);
    }

    public HttpResponse<String> getRetry(final Map<String, String> headers){
        return httpClient.get(uriRetry(), headers);
    }

    public HttpResponse<String> putRetry(String payload, final Map<String, String> headers){
        return httpClient.put(uriRetry(), payload, headers);
    }

    private String uriKey() { return this.config.getHost() + RESOURCE_KEY; }
    private String uriNotification() { return this.config.getHost() + RESOURCE_NOTIFICATION; }
    public String uriRetry(){ return this.config.getHost() + RESOURCE_RETRY; }
}
