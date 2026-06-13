package com.recurrence.pagbank.service;

import com.recurrence.contract.SubscriptionService;
import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.contract.filters.SubscriptionFilter;
import com.recurrence.core.BaseService;
import com.recurrence.pagbank.PagbankConfig;
import com.recurrence.pagbank.client.PagBankSubscriptionClient;
import com.recurrence.pagbank.domain.error.PagBankErrorHandler;
import com.recurrence.pagbank.domain.payment.PagBankListInvoice;
import com.recurrence.pagbank.domain.subscription.PagBankSubscription;
import com.recurrence.pagbank.domain.subscription.PagbankListSubscription;
import com.recurrence.pagbank.mapper.PagBankInvoiceMapper;
import com.recurrence.pagbank.mapper.PagBankSubscriptionMapper;
import java.net.http.HttpResponse;
import java.util.Map;

import com.recurrence.pagbank.validator.SubscrptionValidator;

import static com.recurrence.constants.ProviderConstants.OPERATION_EXCEPTION;
import static com.recurrence.constants.ProviderConstants.PAGBANK;

public class PagBankSubscriptionService extends BaseService implements SubscriptionService {
    private final PagbankConfig config;
    private final PagBankSubscriptionClient pagBankSubscriptionClient;

    public PagBankSubscriptionService(
            final PagbankConfig config
    ) {
        this.config = config;
        pagBankSubscriptionClient = new PagBankSubscriptionClient(config);
    }

    @Override
    public RequestResponse<Subscription> create(Subscription subscription) {
        return create(subscription, RequestOptions.empty());
    }

    @Override
    public RequestResponse<Subscription> create(Subscription subscription, RequestOptions requestOptions){
        try {
            SubscrptionValidator.validateCreateRequest(subscription);
            HttpResponse<String> response = pagBankSubscriptionClient.create(
                    toJson(PagBankSubscriptionMapper.toPagBankContract(subscription)),
                    resolveIdempotencyKey(config.fullHeaders(), requestOptions)
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
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Subscription> get(String id) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.get(id, config.fullHeaders());
            RequestResponse<Subscription> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 2 -> requestResponse.setBody(
                    PagBankSubscriptionMapper.toContract(fromJson(response.body(), PagBankSubscription.class))
                );
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Subscription>> list(){
        return list(SubscriptionFilter
                .builder()
                .build()
        );
    }

    @Override
    public RequestResponse<PageResponse<Subscription>> list(SubscriptionFilter subscriptionFilter) {
        return list(
            subscriptionFilter,
            RequestOptions.empty()
        );
    }

    @Override
    public RequestResponse<PageResponse<Subscription>> list(SubscriptionFilter subscriptionFilter, RequestOptions requestOptions){
        try {
            SubscriptionFilter filter = subscriptionFilter == null
                ? SubscriptionFilter.builder().build()
                : subscriptionFilter;
            RequestOptions options = requestOptions == null
                ? RequestOptions.empty()
                : requestOptions;
            Map<String, String> headers = config.fullHeaders();
            applyQueryFilter(headers, filter);
            HttpResponse<String> response = pagBankSubscriptionClient.list(
                    filter,
                    options,
                    headers
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
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Invoice>> listInvoiceBySubscription(String subscriptionId, int offset, int limit) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.listInvoices(subscriptionId, config.fullHeaders());
            RequestResponse<PageResponse<Invoice>> requestResponse = buildResponse(response);

            return switch ((requestResponse.getHttpResponse()/100)) {
                case 2 -> requestResponse.setBody(
                    PagBankInvoiceMapper.toInvoicePageResponse(fromJson(response.body(), PagBankListInvoice.class))
                );
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Subscription> update(String id, Subscription subscription) {
        return update(id, subscription, RequestOptions.builder().build());
    }

    @Override
    public RequestResponse<Subscription> update(String id, Subscription subscription, RequestOptions requestOptions){
        try {
            var payload = toJson(PagBankSubscriptionMapper.toPagBankContract(subscription));
            var headers = resolveIdempotencyKey(config.fullHeaders(), requestOptions);
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
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Void> suspend(String id) {
        return suspend(id, RequestOptions.builder().build());
    }

    @Override
    public RequestResponse<Void> suspend(String id, RequestOptions requestOptions){
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.suspend(id, resolveIdempotencyKey(config.fullHeaders(), requestOptions));
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Void> cancel(String id) {
        return cancel(id, RequestOptions.builder().build());
    }

    @Override
    public RequestResponse<Void> cancel(String id, RequestOptions requestOptions) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.cancel(id, resolveIdempotencyKey(config.fullHeaders(), requestOptions));
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }


    @Override
    public RequestResponse<Void> active(String id) {
        return active(id, RequestOptions.builder().build());
    }

    @Override
    public RequestResponse<Void> active(String id, RequestOptions requestOptions) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.activate(id, resolveIdempotencyKey(config.fullHeaders(), requestOptions));
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }


    @Override
    public RequestResponse<Void> deleteCoupon(String id) {
        return deleteCoupon(id, RequestOptions.builder().build());
    }

    @Override
    public RequestResponse<Void> deleteCoupon(String id, RequestOptions requestOptions) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.deleteCoupons(id, resolveIdempotencyKey(config.fullHeaders(), requestOptions));
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }


    @Override
    public RequestResponse<Void> retry(String id) {
        return retry(id, RequestOptions.builder().build());
    }

    @Override
    public RequestResponse<Void> retry(String id, RequestOptions requestOptions) {
        try {
            HttpResponse<String> response = pagBankSubscriptionClient.retry(id, resolveIdempotencyKey(config.fullHeaders(), requestOptions));
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch ((requestResponse.getHttpResponse()/100)) {
                case 4 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }


    private void applyQueryFilter(Map<String, String> headers, SubscriptionFilter subscriptionFilter) {
        if (subscriptionFilter == null) {
            return;
        }

        if (subscriptionFilter.getReferenceId() != null && !subscriptionFilter.getReferenceId().isBlank()) {
            headers.put("q", subscriptionFilter.getReferenceId());
        }
    }

    private Map<String, String> resolveIdempotencyKey(Map<String, String> headers, RequestOptions requestOptions){
        if(requestOptions.hasIdempotencyKey()){
            headers.put("x-idempotency-key", requestOptions.getIdempotencyKey());
        }
        return headers;
    }
}
