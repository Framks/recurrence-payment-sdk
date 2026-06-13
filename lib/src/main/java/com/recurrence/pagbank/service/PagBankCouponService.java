package com.recurrence.pagbank.service;

import com.recurrence.contract.CouponService;
import com.recurrence.contract.domain.Coupon;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.core.BaseService;
import com.recurrence.pagbank.PagbankConfig;
import com.recurrence.pagbank.client.PagBankCouponClient;
import com.recurrence.pagbank.domain.commons.PagBankList;
import com.recurrence.pagbank.domain.coupon.PagBankCoupon;
import com.recurrence.pagbank.mapper.PagBankCouponMapper;
import com.recurrence.pagbank.domain.error.PagBankErrorHandler;
import com.recurrence.pagbank.domain.coupon.PagBankListCoupon;
import java.net.http.HttpResponse;
import java.util.Map;

import static com.recurrence.constants.ProviderConstants.OPERATION_EXCEPTION;
import static com.recurrence.constants.ProviderConstants.PAGBANK;
import static com.recurrence.pagbank.validator.CouponValidator.validateCreateRequest;

public class PagBankCouponService extends BaseService implements CouponService {

    private final PagbankConfig config;
    private final PagBankCouponClient pagBankCouponClient;

    public PagBankCouponService(
            final PagbankConfig config
    ) {
        this.config = config;
        pagBankCouponClient = new PagBankCouponClient(config);
    }

    @Override
    public RequestResponse<Coupon> create(Coupon coupon) {
        return create(coupon, RequestOptions.builder().build());
    }
    @Override
    public RequestResponse<Coupon> create(Coupon coupon, RequestOptions requestOptions) {
        try{
            validateCreateRequest(coupon);
            String payload = toJson(PagBankCouponMapper.toPagBankContract(coupon));

            var headers = resolveIdempotencyKey(config.fullHeaders(), requestOptions);

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
            throw providerException( PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Coupon> get(String id) {
        try {
            var headers = config.fullHeaders();
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
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Coupon>> list(Integer offset, Integer limit) {
        try {
            var headers = config.fullHeaders();
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
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Void> activate(String id) {
        return activate(id, RequestOptions.builder().build());
    }
    @Override
    public RequestResponse<Void> activate(String id, RequestOptions requestOptions) {
        try {
            var headers = resolveIdempotencyKey(config.fullHeaders(), requestOptions);
            HttpResponse<String> response = pagBankCouponClient.activate(id, headers);
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse;
                case 400 ->
                        PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Void> inactivate(String id) {
        return inactivate(id, RequestOptions.builder().build());
    }
    @Override
    public RequestResponse<Void> inactivate(String id, RequestOptions requestOptions) {
        try {
            var headers = resolveIdempotencyKey(config.fullHeaders(), requestOptions);
            HttpResponse<String> response = pagBankCouponClient.inactivate(id, headers);
            RequestResponse<Void> requestResponse = buildResponse(response);
            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse;
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    private Map<String, String> resolveIdempotencyKey(Map<String, String> headers, RequestOptions requestOptions){
        if(requestOptions.hasIdempotencyKey()){
            headers.put("x-idempotency-key", requestOptions.getIdempotencyKey());
        }
        return headers;
    }

}
