package com.recurrence.pagbank.validator;

import com.recurrence.contract.domain.Subscription;
import com.recurrence.exception.RecurrenceValidationException;

public final class SubscrptionValidator {
    private SubscrptionValidator(){}

    public static void validateCreateRequest(Subscription request){
        if (request == null) {
            throw new RecurrenceValidationException("invalid request, subscription is required.");
        }

        if (request.getReferenceId() == null || request.getReferenceId().isBlank()) {
            throw new RecurrenceValidationException("invalid request, referenceId is required.");
        }

        if (request.getPlan() == null) {
            throw new RecurrenceValidationException("invalid request, plan is required.");
        }

        if (request.getCustomer() == null) {
            throw new RecurrenceValidationException("invalid request, customer is required.");
        }

        if (request.getPaymentMethod() == null || request.getPaymentMethod().isEmpty()) {
            throw new RecurrenceValidationException("invalid request, payment_method must have at least one item.");
        }
    }
}
