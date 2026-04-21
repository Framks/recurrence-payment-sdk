package com.recurrence.pagbank.service;

import com.recurrence.contract.SubscriptionService;
import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.core.BaseService;
import com.recurrence.pagbank.PagBankEnvironment;
import com.recurrence.pagbank.client.PagBankSubscriptionClient;
import com.recurrence.pagbank.domain.error.PagBankErrorHandler;
import com.recurrence.pagbank.domain.payment.PagBankListInvoice;
import com.recurrence.pagbank.domain.subscription.PagBankSubscription;
import com.recurrence.pagbank.domain.subscription.PagbankListSubscription;
import com.recurrence.pagbank.mapper.PagBankInvoiceMapper;
import com.recurrence.pagbank.mapper.PagBankSubscriptionMapper;
import java.net.http.HttpResponse;
import java.util.Objects;

public class PagBankSubscriptionService extends BaseService implements SubscriptionService {
    private final String token;
    private final PagBankSubscriptionClient pagBankSubscriptionClient;

    public PagBankSubscriptionService(
        final PagBankEnvironment environment,
        final String token
    ) {
        this.token = Objects.requireNonNull(token, "Token must be a valid value");
        pagBankSubscriptionClient = new PagBankSubscriptionClient(Objects.requireNonNull(environment, "Environment must be a valid value"));
    }

    @Override
    public RequestResponse<Subscription> create(Subscription subscription) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.create(
                toJson(PagBankSubscriptionMapper.toPagBankContract(subscription)),
                defaultHeaders(token)
            );

            RequestResponse<Subscription> requestResponse = buildResponse(response);

            return switch((requestResponse.getHttpResponse()/100)){
                case 2 -> requestResponse.setBody(
                    PagBankSubscriptionMapper.toContract(fromJson(response.body(), PagBankSubscription.class))
                );
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Subscription> get(String id) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.get(id, defaultHeaders(token));
            RequestResponse<Subscription> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 2 -> requestResponse.setBody(
                    PagBankSubscriptionMapper.toContract(fromJson(response.body(), PagBankSubscription.class))
                );
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Subscription>> list(int offset, int limit, String referenceId) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.list(
                null,
                null,
                null,
                null,
                offset,
                limit,
                defaultHeaders(token),
                referenceId
            );

            RequestResponse<PageResponse<Subscription>> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 2 -> requestResponse.setBody(
                    PagBankSubscriptionMapper.toPageResponse(fromJson(response.body(), PagbankListSubscription.class))
                );
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Invoice>> listInvoiceBySubscription(String subscriptionId, int offset, int limit) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.listInvoices(subscriptionId, defaultHeaders(token));
            RequestResponse<PageResponse<Invoice>> requestResponse = buildResponse(response);

            return switch ((requestResponse.getHttpResponse()/100)) {
                case 2 -> requestResponse.setBody(
                    PagBankInvoiceMapper.toInvoicePageResponse(fromJson(response.body(), PagBankListInvoice.class))
                );
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Subscription> update(String id, Subscription subscription) {
        try {
            var payload = toJson(PagBankSubscriptionMapper.toPagBankContract(subscription));
            var headers = defaultHeaders(token);
            HttpResponse<String> response = pagBankSubscriptionClient.put(id, payload, headers);
            RequestResponse<Subscription> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 2 -> requestResponse.setBody(
                    PagBankSubscriptionMapper.toContract(fromJson(response.body(), PagBankSubscription.class))
                );
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Void> suspend(String id) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.suspend(id, defaultHeaders(token));
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Void> cancel(String id) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.cancel(id, defaultHeaders(token));
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Void> active(String id) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.activate(id, defaultHeaders(token));
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Void> deleteCoupon(String id) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.deleteCoupons(id, defaultHeaders(token));
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Void> retry(String id) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.retry(id, defaultHeaders(token));
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
