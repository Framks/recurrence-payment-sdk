package com.recurrence.pagbank.validator;

import com.recurrence.contract.domain.Coupon;
import com.recurrence.exception.RecurrenceValidationException;

public final class CouponValidator {
    private CouponValidator(){}

    public static void validateCreateRequest(Coupon request){
        if (request == null) {
            throw new RecurrenceValidationException("invalid request, coupon is required.");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new RecurrenceValidationException("invalid request, name is required.");
        }

        if (request.getDiscount() == null) {
            throw new RecurrenceValidationException("invalid request, discount is required.");
        }

        if (request.getDiscount().type() == null || request.getDiscount().type().isBlank()) {
            throw new RecurrenceValidationException("invalid request, Discount.type is required.");
        }

        if (request.getDiscount().value() == null) {
            throw new RecurrenceValidationException("invalid request, Discount.value is required.");
        }

        if (request.getDiscount().value() <= 0) {
            throw new RecurrenceValidationException("invalid request, Discount.value has been more that 0.");
        }

        if (request.getDuration() == null) {
            throw new RecurrenceValidationException("invalid request, duration is required.");
        }

        if (request.getDuration().type() == null || request.getDuration().type().isBlank()) {
            throw new RecurrenceValidationException("invalid request, Duration.type is required.");
        }

        if (request.getRedemptionLimit() == null) {
            throw new RecurrenceValidationException("invalid request, redemption_limit is required.");
        }

        if (request.getRedemptionLimit() <= 0) {
            throw new RecurrenceValidationException("invalid request, redemption_limit has been more that 0.");
        }
    }
}
