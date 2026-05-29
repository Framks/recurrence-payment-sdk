package com.recurrence.pagbank.service;

import com.recurrence.contract.CustomerService;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.Customer;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.core.BaseService;
import com.recurrence.pagbank.PagBankEnvironment;
import com.recurrence.pagbank.client.PagBankCustomerClient;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import com.recurrence.pagbank.domain.customer.PagBankListCustomer;
import com.recurrence.pagbank.domain.error.PagBankErrorHandler;
import com.recurrence.pagbank.mapper.CommonsMapper;
import com.recurrence.pagbank.mapper.PagbankCustomerMapper;
import java.net.http.HttpResponse;
import java.util.Objects;

import static com.recurrence.pagbank.validator.CustomerValidator.validateCreateRequest;

public class PagbankCustomerService extends BaseService implements CustomerService {

    private final PagBankCustomerClient pagBankCustomerClient;
    private final String token;

    public PagbankCustomerService(
            PagBankEnvironment environment,
            String token
    ){
        this.token = Objects.requireNonNull(token, "Token must be a valid value");
        pagBankCustomerClient = new PagBankCustomerClient(Objects.requireNonNull(environment, "Environment must be a valid value"));
    }

    @Override
    public RequestResponse<Customer> create(Customer customer) {
        try{
            validateCreateRequest(customer);
            String payload = toJson(PagbankCustomerMapper.from(customer));
            var headers = defaultHeaders(token);
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
            throw providerException("PagBank", "customer operation", e);
        }
    }

    @Override
    public RequestResponse<Customer> get(String id) {
        try{
            var headers = defaultHeaders(token);
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
            throw providerException("PagBank", "customer operation", e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Customer>> list(Integer offset, Integer limit, String query) {
        try {
            var headers = defaultHeaders(token);
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
            throw providerException("PagBank", "customer operation", e);
        }
    }

    @Override
    public RequestResponse<Customer> update(String id, Customer customer) {
        try {
            String payload = toJson(PagbankCustomerMapper.toUpdate(customer));
            var headers = defaultHeaders(token);
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
            throw providerException("PagBank", "customer operation", e);
        }
    }

    @Override
    public RequestResponse<Customer> updateBillingInfo(String id, BillingInfo billingInfo) {
        if (billingInfo == null){
            throw new IllegalArgumentException("BillingInfo must be a valid value");
        }
        try {
            String payload = toJson(CommonsMapper.toPagBankBillingInfo(billingInfo));
            var headers = defaultHeaders(token);
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
            throw providerException("PagBank", "customer operation", e);
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
}
