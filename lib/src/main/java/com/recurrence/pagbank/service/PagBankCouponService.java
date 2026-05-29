package com.recurrence.pagbank.service;

import com.recurrence.contract.CouponService;
import com.recurrence.contract.domain.Coupon;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.core.BaseService;
import com.recurrence.pagbank.PagBankEnvironment;
import com.recurrence.pagbank.client.PagBankCouponClient;
import com.recurrence.pagbank.domain.coupon.PagBankCoupon;
import com.recurrence.pagbank.mapper.PagBankCouponMapper;
import com.recurrence.pagbank.domain.error.PagBankErrorHandler;
import com.recurrence.pagbank.domain.coupon.PagBankListCoupon;
import java.net.http.HttpResponse;
import java.util.Objects;
import static com.recurrence.pagbank.validator.CouponValidator.validateCreateRequest;

public class PagBankCouponService extends BaseService implements CouponService {

    private final String token;
    private final PagBankCouponClient pagBankCouponClient;

    public PagBankCouponService(
            final PagBankEnvironment environment,
            final String token
    ) {
        this.token = Objects.requireNonNull(token, "Token must be a valid value");

        pagBankCouponClient = new PagBankCouponClient(Objects.requireNonNull(environment, "Environment must be a valid value"));
    }

    @Override
    public RequestResponse<Coupon> create(Coupon coupon) {
        try{
            validateCreateRequest(coupon);
            String payload = toJson(PagBankCouponMapper.toPagBankContract(coupon));

            var headers = defaultHeaders(token);

            HttpResponse<String> response = pagBankCouponClient.create(payload, headers);

            RequestResponse<Coupon> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 201 -> requestResponse.setBody(
                        PagBankCouponMapper.toContract(fromJson(response.body(), PagBankCoupon.class))
                );

                case 400 ->
                        PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException("PagBank", "coupon operation", e);
        }
    }

    @Override
    public RequestResponse<Coupon> get(String id) {
        try {
            var headers = defaultHeaders(token);
            HttpResponse<String> response = pagBankCouponClient.get(id, headers);
            RequestResponse<Coupon> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse.setBody(
                        PagBankCouponMapper.toContract(fromJson(response.body(), PagBankCoupon.class))
                );

                case 400 ->
                        PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException("PagBank", "coupon operation", e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Coupon>> list(Integer offset, Integer limit) {
        try {
            var headers = defaultHeaders(token);
            HttpResponse<String> response = pagBankCouponClient.list(offset, limit, headers);
            RequestResponse<PageResponse<Coupon>> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse.setBody(
                        PagBankCouponMapper.toPageResponse(fromJson(response.body(), PagBankListCoupon.class))
                );
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e){
            throw providerException("PagBank", "coupon operation", e);
        }
    }

    @Override
    public RequestResponse<Void> activate(String id) {
        try {
            var headers = defaultHeaders(token);
            HttpResponse<String> response = pagBankCouponClient.activate(id, headers);
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse;
                case 400 ->
                        PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException("PagBank", "coupon operation", e);
        }
    }

    @Override
    public RequestResponse<Void> inactivate(String id) {
        try {
            var headers = defaultHeaders(token);
            HttpResponse<String> response = pagBankCouponClient.inactivate(id, headers);
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse;
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException("PagBank", "coupon operation", e);
        }
    }

}
