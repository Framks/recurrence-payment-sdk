package com.recurrence.pagbank.service;

import com.recurrence.contract.CustomerService;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.Customer;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.core.BaseService;
import com.recurrence.pagbank.PagbankConfig;
import com.recurrence.pagbank.client.PagBankCustomerClient;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import com.recurrence.pagbank.domain.customer.PagBankListCustomer;
import com.recurrence.pagbank.domain.error.PagBankErrorHandler;
import com.recurrence.pagbank.mapper.CommonsMapper;
import com.recurrence.pagbank.mapper.PagbankCustomerMapper;
import java.net.http.HttpResponse;
import java.util.Map;

import static com.recurrence.constants.ProviderConstants.OPERATION_EXCEPTION;
import static com.recurrence.constants.ProviderConstants.PAGBANK;
import static com.recurrence.pagbank.validator.CustomerValidator.validateCreateRequest;

public class PagbankCustomerService extends BaseService implements CustomerService {

    private final PagBankCustomerClient pagBankCustomerClient;
    private final PagbankConfig config;

    public PagbankCustomerService(
            final PagbankConfig config
    ){
        this.config = config;
        pagBankCustomerClient = new PagBankCustomerClient(config);
    }

    @Override
    public RequestResponse<Customer> create(Customer customer) {
        return create(customer, RequestOptions.builder().build());
    }

    @Override
    public RequestResponse<Customer> create(Customer customer, RequestOptions requestOptions) {
        try{
            validateCreateRequest(customer);
            String payload = toJson(PagbankCustomerMapper.from(customer));
            var headers = resolveIdempotencyKey(config.fullHeaders(), requestOptions);
            HttpResponse<String> response = pagBankCustomerClient.create(payload, headers);
            RequestResponse<Customer> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 201 -> requestResponse.setBody(
                    PagbankCustomerMapper.to(fromJson(response.body(), PagBankCustomer.class))
                );
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Customer> get(String id) {
        try{
            var headers = config.fullHeaders();
            HttpResponse<String> response = pagBankCustomerClient.get(id, headers);
            RequestResponse<Customer> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()){
                case 200 -> requestResponse.setBody(
                    PagbankCustomerMapper.to(fromJson(response.body(), PagBankCustomer.class))
                );

                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        }catch (Exception e){
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Customer>> list(Integer offset, Integer limit, String query) {
        try {
            var headers = config.fullHeaders();
            if (query != null && !query.isBlank()){
                headers.put("q",normalizeString(query));
            }
            HttpResponse<String> response = pagBankCustomerClient.list(offset, limit, headers);
            RequestResponse<PageResponse<Customer>> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()){
                case 200 -> requestResponse.setBody(
                    PagbankCustomerMapper.toPagResponse(fromJson(response.body(), PagBankListCustomer.class))  // todo fazer um dto que tem a listagem generica para cada tipo do pagbank para não criar uma classe ListXpto para cada tipo de retorno.
                );
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Customer> update(String id, Customer customer) {
        return update(id, customer, RequestOptions.builder().build());
    }

    @Override
    public RequestResponse<Customer> update(String id, Customer customer, RequestOptions requestOptions) {
        try {
            String payload = toJson(PagbankCustomerMapper.toUpdate(customer));
            var headers = resolveIdempotencyKey(config.fullHeaders(), requestOptions);
            HttpResponse<String> response = pagBankCustomerClient.put(id, payload, headers);
            RequestResponse<Customer> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()){
                case 200 -> requestResponse.setBody(
                    PagbankCustomerMapper.to(fromJson(response.body(), PagBankCustomer.class))
                );
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Customer> updateBillingInfo(String id, BillingInfo billingInfo) {
        return updateBillingInfo(id, billingInfo, RequestOptions.builder().build());
    }

    @Override
    public RequestResponse<Customer> updateBillingInfo(String id, BillingInfo billingInfo, RequestOptions requestOptions) {
        if (billingInfo == null){
            throw new IllegalArgumentException("BillingInfo must be a valid value");
        }
        // Todo fazer o validate do billling info
        try {
            String payload = toJson(CommonsMapper.toPagBankBillingInfo(billingInfo));
            var headers = resolveIdempotencyKey(config.fullHeaders(), requestOptions);
            HttpResponse<String> response = pagBankCustomerClient.putBillingInfo(id, payload, headers);
            RequestResponse<Customer> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()) {
                case 201 -> requestResponse.setBody(
                    PagbankCustomerMapper.to(fromJson(response.body(), PagBankCustomer.class))
                );
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    private String normalizeString(String query){
        if (query == null || query.isBlank()) {
            return "";
        }
        String normalized = query.replaceAll("[^\\p{L}0-9@._\\- ]", "");

        normalized = normalized.replaceAll("\\s+", " ");

        return normalized.trim();
    }

    private Map<String, String> resolveIdempotencyKey(Map<String, String> headers, RequestOptions requestOptions){
        if(requestOptions.hasIdempotencyKey()){
            headers.put("x-idempotency-key", requestOptions.getIdempotencyKey());
        }
        return headers;
    }
}
