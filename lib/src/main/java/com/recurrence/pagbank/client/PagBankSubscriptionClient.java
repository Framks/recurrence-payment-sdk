package com.recurrence.pagbank.client;

import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.contract.infra.SubscriptionFilter;
import com.recurrence.infrastructure.HttpClientAdapter;
import com.recurrence.pagbank.PagbankConfig;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PagBankSubscriptionClient {

    private final HttpClientAdapter httpClient = new HttpClientAdapter();
    private final PagbankConfig config;
    private static final String RESOURCE = "/subscriptions";

    public PagBankSubscriptionClient(PagbankConfig config) {
        this.config = config;
    }

    public HttpResponse<String> create(final String payload, final Map<String, String> headers) {
        return httpClient.post(uriCreate(), payload, headers);
    }

    public HttpResponse<String> get(final String id, final Map<String, String> headers) {
        return httpClient.get(uriGet(id), headers);
    }

    public HttpResponse<String> list(
            SubscriptionFilter subscriptionFilter,
            RequestOptions requestOptions, // devemos evoluir no futuro para todas as chamadas possibilitarem sobrescrita de configuração default.
            final Map<String, String> headers
    ) {
        return httpClient.get(uriList(subscriptionFilter), headers);
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
        return this.config.getHost() + RESOURCE;
    }

    private String uriList(
            SubscriptionFilter subscriptionFilter
    ) {
        // debugando essa função foi possivel identificar que ele não esta retornando certo
        // saida https://alguma.coisa/chiqui-chiqui/?offset=10&limit=10&payment_method=DENIED&payment_method=APPROVED&created_at_start=2026-05-29T18%3A24%3A01.936491822Z&updated_at_end=2026-05-29T18%3A24%3A01.936491822Z&reference_id=ALGO_UUID
        // problema identificado o payment_method é uma lista então ele como o
        String uri = this.config.getHost() + RESOURCE;

        if (subscriptionFilter == null) return uri;

        List<String> params = new ArrayList<>();

        addParam(params, "offset", subscriptionFilter.getOffset());
        addParam(params, "limit", subscriptionFilter.getLimit());
        addParams(params, "status", subscriptionFilter.getStatus());
        addParams(params, "payment_method", subscriptionFilter.getPaymentMethods());
        addParam(params, "created_at_start", tranformeDate(subscriptionFilter.getCreatedAtStart()));
        addParam(params, "created_at_end", tranformeDate(subscriptionFilter.getCreatedAtEnd()));
        addParam(params, "reference_id", subscriptionFilter.getReferenceId());

        if (params.isEmpty()) return uri;

        String url = uri + "?" + String.join("&", params); // toDo remover
        System.out.println(url);

        return url;
    }

    private String uriGet(String id) {
        return this.config.getHost() + RESOURCE + "/" + id;
    }

    private String uriListInvoices(String id) {
        return this.config.getHost() + RESOURCE + "/" + id + "/invoices";
    }

    private String uriCancel(String id) {
        return this.config.getHost() + RESOURCE + "/" + id + "/cancel";
    }

    private String uriSuspend(String id) {
        return this.config.getHost() + RESOURCE + "/" + id + "/suspend";
    }

    private String uriActivate(String id) {
        return this.config.getHost() + RESOURCE + "/" + id + "/activate";
    }

    private String uriDeleteCoupons(String id) {
        return this.config.getHost() + RESOURCE + "/" + id + "/coupons";
    }

    private String uriRetry(String id) {
        return this.config.getHost() + RESOURCE + "/" + id + "/retry";
    }

    private void addParam(List<String> params, String name, Object value) {
        if (value == null) {
            return;
        }

        String rawValue = String.valueOf(value);
        if (rawValue.isBlank()) {
            return;
        }

        params.add(encode(name) + "=" + encode(rawValue));
    }

    private void addParams(List<String> params, String name, List<?> values) {
        if (values == null || values.isEmpty()) {
            return;
        }

        values.forEach(value -> addParam(params, name, value));
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String tranformeDate(OffsetDateTime dateTime){ // ToDo remover para uma clase mais adequada.
        if (dateTime == null) return null;

        return dateTime
                .toLocalDate()
                .format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
