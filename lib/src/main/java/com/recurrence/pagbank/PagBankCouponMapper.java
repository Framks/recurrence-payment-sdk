package com.recurrence.pagbank;

import com.recurrence.contract.domain.Coupon;
import com.recurrence.pagbank.domain.coupon.PagBankCoupon;
import com.recurrence.pagbank.domain.coupon.PagBankCouponDiscount;
import com.recurrence.pagbank.domain.coupon.PagBankCouponDuration;

public final class PagBankCouponMapper {

    private PagBankCouponMapper() { }

    public static PagBankCoupon from(final Coupon coupon) {
        return new PagBankCoupon.Builder()
                .referenceId(coupon.getReferenceId())
                .name(coupon.getName())
                .description(coupon.getDescription())
                .discount(mapDiscount(coupon))
                .duration(mapDuration(coupon))
                .redemptionLimit(coupon.getRedemptionLimit())
                .expiresAt(coupon.getExpiresAt())
                .build();
    }

    private static PagBankCouponDuration mapDuration(Coupon coupon) {
        if (coupon.getDuration() == null) {
            return null;
        }

        return new PagBankCouponDuration(
                coupon.getDuration().type(),
                coupon.getDuration().occurrences()
        );
    }

    private static PagBankCouponDiscount mapDiscount(Coupon coupon) {
        if (coupon.getDiscount() == null) {
            return null;
        }

        return new PagBankCouponDiscount(
                coupon.getDiscount().value(),
                coupon.getDiscount().type()
        );
    }
}
