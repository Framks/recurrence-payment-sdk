package com.recurrence.pagbank;

import com.recurrence.contract.CouponService;
import com.recurrence.contract.domain.Coupon;
import com.recurrence.infrastructure.HttpClientAdapter;
import com.recurrence.pagbank.domain.coupon.PagBankCoupon;
import java.util.Map;
import java.util.Objects;

public class PagBankCouponService implements CouponService {

    private final PagBankEnvironment environment;
    private final String token;
    private final HttpClientAdapter httpClient = new HttpClientAdapter();

    public PagBankCouponService(
            final PagBankEnvironment environment,
            final String token
    ) {
        this.environment = Objects.requireNonNull(environment, "Environment must be a valid value");
        this.token = Objects.requireNonNull(token, "Token must be a valid value");
    }

    @Override
    public Coupon create(Coupon coupon) {

        PagBankCoupon pagBankCoupon = PagBankCouponMapper.from(coupon);

        final String endpoint = environment.getHost() + "/coupons";

        String payload = ""; // ToDo montar o payload JSON com os dados do pagBankCoupon usando o jackson

        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "Authorization", "Bearer " + token
        );

        // ToDo implementar a chamada para criação

        return null;
    }
}
