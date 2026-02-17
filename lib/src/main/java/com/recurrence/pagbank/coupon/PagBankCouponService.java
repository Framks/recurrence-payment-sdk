package com.recurrence.pagbank.coupon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recurrence.contract.CouponService;
import com.recurrence.contract.domain.Coupon;
import com.recurrence.contract.domain.ListCoupon;
import com.recurrence.contract.domain.infra.RequestResponse;
import com.recurrence.core.Converter;
import com.recurrence.pagbank.PagBankEnvironment;
import com.recurrence.pagbank.domain.coupon.PagBankCoupon;
import com.recurrence.pagbank.domain.coupon.PagBankCouponMapper;
import com.recurrence.pagbank.domain.error.PagBankErrorList;
import com.recurrence.pagbank.domain.error.PagBankErrorMapper;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PagBankCouponService implements CouponService {

    private final PagBankEnvironment environment;
    private final String token;
    private final PagBankCouponClient pagBankCouponClient;

    public PagBankCouponService(
            final PagBankEnvironment environment,
            final String token
    ) {
        this.environment = Objects.requireNonNull(environment, "Environment must be a valid value");
        this.token = Objects.requireNonNull(token, "Token must be a valid value");

        pagBankCouponClient = new PagBankCouponClient(environment);
    }

    @Override
    public RequestResponse<Coupon> create(Coupon coupon) {

        try{
            String payload = createPayload(coupon);

            var headers = createHeaders(token);

            HttpResponse<String> response = pagBankCouponClient.create(payload, headers);

            RequestResponse requestResponse = createRequestResponse(response);

            switch (requestResponse.getHttpResponse()) {
                case 201:
                    createSuccessResponse(requestResponse, response);
                    break;
                case 400:
                    createErrorResponse(requestResponse, response);
                    break;
            }

            return requestResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Coupon> get(String id) {
        return null;
    }

    @Override
    public RequestResponse<ListCoupon> list(Long offset, Long limit) {
        return null;
    }

    @Override
    public RequestResponse<Coupon> update(String id, Coupon coupon) {
        return null;
    }

    @Override
    public RequestResponse<Void> activate(String id) {
        return null;
    }

    @Override
    public RequestResponse<Void> inactivate(String id) {
        return null;
    }

    private static String createPayload(Coupon coupon) throws JsonProcessingException {
        return Converter.mapper().writeValueAsString(PagBankCouponMapper.from(coupon));
    }

    private Map<String, String> createHeaders(final String token) {
        return Map.of(
                "Authorization", "Bearer " + token,
                "Content-Type", "application/json",
                "accept", "application/json"
        );
    }

    private RequestResponse createRequestResponse(HttpResponse<String> response) {
        return new RequestResponse<>(
                response.statusCode(),
                getHeaders(response)
        );
    }

    private List<Map<String, String>> getHeaders(HttpResponse<String> response) {
        return response.headers()
                .map()
                .entrySet()
                .stream()
                .flatMap(entry -> entry.getValue()
                        .stream()
                        .map(value -> {
                            Map<String, String> headerMap = new HashMap<>();
                            headerMap.put(entry.getKey(), value);
                            return headerMap;
                        })
                )
                .toList();
    }

    private RequestResponse createErrorResponse(RequestResponse requestResponse, HttpResponse<String> response) throws JsonProcessingException {
        return requestResponse.setErrors(PagBankErrorMapper.to(Converter.mapper().readValue(response.body(), PagBankErrorList.class)));
    }

    private RequestResponse createSuccessResponse( RequestResponse requestResponse, HttpResponse<String> response) throws JsonProcessingException {
        return requestResponse.setBody(PagBankCouponMapper.to(Converter.mapper().readValue(response.body(), PagBankCoupon.class)));
    }
}
