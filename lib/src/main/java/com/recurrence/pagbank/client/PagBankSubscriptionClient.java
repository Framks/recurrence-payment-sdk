package com.recurrence.pagbank.client;

import com.recurrence.infrastructure.HttpClientAdapter;
import com.recurrence.pagbank.PagBankEnvironment;
import java.net.http.HttpResponse;
import java.time.OffsetDateTime;
import java.util.Map;

public class PagBankSubscriptionClient {

    private final HttpClientAdapter httpClient = new HttpClientAdapter();
    private final PagBankEnvironment environment;
    private static final String RESOURCE = "/subscriptions";

    public PagBankSubscriptionClient(PagBankEnvironment environment) {
        this.environment = environment;
    }

    public HttpResponse<String> create(final String payload, final Map<String, String> headers) {
        return httpClient.post(uriCreate(), payload, headers);
    }

    public HttpResponse<String> get(final String id, final Map<String, String> headers) {
        return httpClient.get(uriGet(id), headers);
    }

    public HttpResponse<String> list(
            String status,
            String paymentMethodType,
            OffsetDateTime createdAtStart,
            OffsetDateTime createdAtEnd,
            int offset,
            int limit,
            final Map<String, String> headers,
            String referenceId
    ) {
        System.out.println("Listando assinaturas com os seguintes parâmetros:");
        System.out.println(uriList(   // transformar os parâmetros em query params array de string
            status,
            paymentMethodType,
            createdAtStart,
            createdAtEnd,
            offset,
            limit
        ));
        System.out.println("Headers:" + headers);
        return httpClient.get(
                uriList(   // transformar os parâmetros em query params array de string
                        status,
                        paymentMethodType,
                        createdAtStart,
                        createdAtEnd,
                        offset,
                        limit
                ),
                headers
        );
    }

    public HttpResponse<String> listInvoices(final String id, final Map<String, String> headers) {   ///  Todo fazer ele ter filtros
        return httpClient.get(uriListInvoices(id), headers);
    }

    public HttpResponse<String> put(final String id, final String payload, final Map<String, String> headers) {
        return httpClient.put(uriGet(id), payload, headers);
    }

    public HttpResponse<String> cancel(final String id, final Map<String, String> headers) {
        return httpClient.put(uriCancel(id), "", headers);
    }

    public HttpResponse<String> suspend(final String id, final Map<String, String> headers) {
        return httpClient.put(uriSuspend(id), "", headers);
    }

    public HttpResponse<String> activate(final String id, final Map<String, String> headers) {
        return httpClient.put(uriActivate(id), "", headers);
    }

    public HttpResponse<String> deleteCoupons(final String id, final Map<String, String> headers) {
        return httpClient.delete(uriDeleteCoupons(id), headers);
    }

    public HttpResponse<String> retry(final String id, final Map<String, String> headers) {
        return httpClient.post(uriRetry(id), "", headers);
    }


    private String uriCreate() {
        return this.environment.getHost() + RESOURCE;
    }

    private String uriList(  /// todo  transformar os parâmetros em query params array de string
            String status,
            String paymentMethodType,
            OffsetDateTime createdAtStart,
            OffsetDateTime createdAtEnd,
            int offset,
            int limit
    ) {
        return this.environment.getHost() + RESOURCE + "?offset=" + offset + "&limit=" + limit;
    }

    private String uriGet(String id) {
        return this.environment.getHost() + RESOURCE + "/" + id;
    }

    private String uriListInvoices(String id) {
        return this.environment.getHost() + RESOURCE + "/" + id + "/invoices";
    }

    private String uriCancel(String id) {
        return this.environment.getHost() + RESOURCE + "/" + id + "/cancel";
    }

    private String uriSuspend(String id) {
        return this.environment.getHost() + RESOURCE + "/" + id + "/suspend";
    }

    private String uriActivate(String id) {
        return this.environment.getHost() + RESOURCE + "/" + id + "/activate";
    }

    private String uriDeleteCoupons(String id) {
        return this.environment.getHost() + RESOURCE + "/" + id + "/coupons";
    }

    private String uriRetry(String id) {
        return this.environment.getHost() + RESOURCE + "/" + id + "/retry";
    }
}