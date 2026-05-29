package com.recurrence.pagbank.client;

import com.recurrence.infrastructure.HttpClientAdapter;
import com.recurrence.pagbank.PagBankEnvironment;
import java.net.http.HttpResponse;
import java.util.Map;

public class PagBankInvoiceClient {

    private final HttpClientAdapter httpClient = new HttpClientAdapter();
    private final PagBankEnvironment environment;
    private static final String RESOURCE_INVOICE = "/invoices";
    private static final String RESOURCE_PAYMENT = "/payments";
    private static final String RESOURCE_REFUND = "/refunds";

    public PagBankInvoiceClient(PagBankEnvironment environment) {
        this.environment = environment;
    }

    public HttpResponse<String> getInvoice(final String id, final Map<String, String> headers) {
        return httpClient.get(uriInvoiceGet(id), headers);
    }

    public HttpResponse<String> listInvoicePayments(final String id, final Map<String, String> headers) {
        return httpClient.get(uriInvoicePayments(id), headers);
    }

    public HttpResponse<String> getPayment(final String id, final Map<String, String> headers) {
        return httpClient.get(uriGetPayment(id), headers);
    }

    public HttpResponse<String> createRefund(final String id, final String payload, final Map<String, String> headers) {
        return httpClient.post(uriCreateRefund(id), payload, headers);
    }

    public HttpResponse<String> getRefund(final String id, final Map<String, String> headers) {
        return httpClient.get(uriGetRefund(id), headers);
    }

    public HttpResponse<String> getRefunds(int offset, int limit, final Map<String, String> headers){
        return httpClient.get(uriGetRefunds(offset, limit), headers);
    }

    public HttpResponse<String> getPaymentRefunds(final String id, final Map<String, String> headers) {
        return httpClient.get(uriCreateRefund(id), headers);
    }

    public HttpResponse<String> listPayments(int offset, int limit, final Map<String, String> headers) {
        return httpClient.get(uriListPayments(offset, limit), headers);
    }


    private String uriInvoiceGet(String id) {
        return this.environment.getHost() + RESOURCE_INVOICE + "/" + id;
    }

    private String uriInvoicePayments(String id) {
        return this.environment.getHost() + RESOURCE_INVOICE + "/" + id + "/payments";
    }

    private String uriGetPayment(String id) {
        return this.environment.getHost() + RESOURCE_PAYMENT + "/" + id;
    }

    private String uriCreateRefund(String id) {
        return this.environment.getHost() + RESOURCE_PAYMENT + "/" + id + "/refunds";
    }

    private String uriGetRefund(String id) {
        return this.environment.getHost() + RESOURCE_REFUND + "/" + id;
    }

    private String uriGetRefunds(int offset, int limit) {
        return this.environment.getHost() + RESOURCE_REFUND + "?offset=" + offset + "&limit=" + limit;
    }

    private String uriListPayments(int offset, int limit) {
        return this.environment.getHost() + RESOURCE_PAYMENT + "?offset=" + offset + "&limit=" + limit;
    }
}
