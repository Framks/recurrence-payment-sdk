package com.recurrence.pagbank.domain.coupon;

import com.recurrence.contract.domain.Coupon;
import com.recurrence.contract.domain.ListCoupon;
import com.recurrence.pagbank.dto.PagBankListCoupon;

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

    public static Coupon to(final PagBankCoupon pagBankCoupon) {
        return new Coupon.Builder()
                .id(pagBankCoupon.getId())
                .referenceId(pagBankCoupon.getReferenceId())
                .name(pagBankCoupon.getName())
                .status(pagBankCoupon.getStatus())
                .inUse(pagBankCoupon.getInUse())
                .description(pagBankCoupon.getDescription())
                .discount(pagBankCoupon.getDiscount().type(), pagBankCoupon.getDiscount().value())
                .duration(pagBankCoupon.getDuration().type(), pagBankCoupon.getDuration().occurrences())
                .redemptionLimit(pagBankCoupon.getRedemptionLimit())
                .expiresAt(pagBankCoupon.getExpiresAt())
                .createAt(pagBankCoupon.getCreatedAt())
                .updatedAt(pagBankCoupon.getUpdatedAt())
                .build();
    }

    public static ListCoupon toListCoupons(final PagBankListCoupon pagBankListCoupon) {
        return ListCoupon.builder()
                .setOffset(pagBankListCoupon.getResultSet().offset())
                .setLimit(pagBankListCoupon.getResultSet().limit())
                .setTotal(pagBankListCoupon.getResultSet().offset())
                .setCoupons(pagBankListCoupon.getCoupons().stream().map(PagBankCouponMapper::to).toList())
                .build();
    }
}
