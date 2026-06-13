package com.recurrence.pagbank.validator;

import com.recurrence.contract.domain.Coupon;
import com.recurrence.exception.RecurrenceValidationException;

import static com.recurrence.constants.ValidationsMessage.COUPON_DISCOUNT_MINOR_ZERO;
import static com.recurrence.constants.ValidationsMessage.COUPON_DISCOUNT_NULL;
import static com.recurrence.constants.ValidationsMessage.COUPON_DISCOUNT_TYPE_NULL;
import static com.recurrence.constants.ValidationsMessage.COUPON_DISCOUNT_VALUE_NULL;
import static com.recurrence.constants.ValidationsMessage.COUPON_DURATION_NULL;
import static com.recurrence.constants.ValidationsMessage.COUPON_DURATION_TYPE_NULL;
import static com.recurrence.constants.ValidationsMessage.COUPON_NAME_NULL;
import static com.recurrence.constants.ValidationsMessage.COUPON_NULL;
import static com.recurrence.constants.ValidationsMessage.COUPON_REDEMPTION_LIMIT_NULL;
import static com.recurrence.constants.ValidationsMessage.COUPON_REDEMPTION_LIMIT_ZERO;

public final class CouponValidator {
    private CouponValidator(){}

    public static void validateCreateRequest(Coupon request){
        if (request == null) {
            throw new RecurrenceValidationException(COUPON_NULL);
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new RecurrenceValidationException(COUPON_NAME_NULL);
        }

        if (request.getDiscount() == null) {
            throw new RecurrenceValidationException(COUPON_DISCOUNT_NULL);
        }

        if (request.getDiscount().type() == null || request.getDiscount().type().isBlank()) {
            throw new RecurrenceValidationException(COUPON_DISCOUNT_TYPE_NULL);
        }

        if (request.getDiscount().value() == null) {
            throw new RecurrenceValidationException(COUPON_DISCOUNT_VALUE_NULL);
        }

        if (request.getDiscount().value() <= 0) {
            throw new RecurrenceValidationException(COUPON_DISCOUNT_MINOR_ZERO);
        }

        if (request.getDuration() == null) {
            throw new RecurrenceValidationException(COUPON_DURATION_NULL);
        }

        if (request.getDuration().type() == null || request.getDuration().type().isBlank()) {
            throw new RecurrenceValidationException(COUPON_DURATION_TYPE_NULL);
        }

        // ToDo quando o type é REPEATING  deve validar se é informado o duration.ocurence

        if (request.getRedemptionLimit() == null) {
            throw new RecurrenceValidationException(COUPON_REDEMPTION_LIMIT_NULL);
        }

        if (request.getRedemptionLimit() <= 0) {
            throw new RecurrenceValidationException(COUPON_REDEMPTION_LIMIT_ZERO);
        }
    }
}
