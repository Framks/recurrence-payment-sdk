package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.Coupon;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.pagbank.domain.coupon.PagBankCoupon;
import com.recurrence.pagbank.domain.coupon.PagBankCouponDiscount;
import com.recurrence.pagbank.domain.coupon.PagBankCouponDuration;
import com.recurrence.pagbank.domain.coupon.PagBankListCoupon;

public final class PagBankCouponMapper {

    private PagBankCouponMapper() { }

    public static PagBankCoupon toPagBankContract(final Coupon coupon) {
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

    public static Coupon toContract(final PagBankCoupon pagBankCoupon) {
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

    public static PageResponse<Coupon> toPageResponse(final PagBankListCoupon pagBankListCoupon) {
        return new PageResponse<>(
                new PageResponse.PageInfo(
                        pagBankListCoupon.getResultSet().offset(),
                        pagBankListCoupon.getResultSet().limit(),
                        pagBankListCoupon.getResultSet().total()
                ),
                pagBankListCoupon.getCoupons().stream().map(PagBankCouponMapper::toContract).toList()
        );
    }
}
