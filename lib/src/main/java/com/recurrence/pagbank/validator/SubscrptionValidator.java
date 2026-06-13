package com.recurrence.pagbank.validator;

import com.recurrence.contract.domain.Subscription;
import com.recurrence.exception.RecurrenceValidationException;
import static com.recurrence.constants.ValidationsMessage.SUBSCRIPTION_CUSTOMER_NULL;
import static com.recurrence.constants.ValidationsMessage.SUBSCRIPTION_NULL;
import static com.recurrence.constants.ValidationsMessage.SUBSCRIPTION_PAYMENT_METHOD_NULL;
import static com.recurrence.constants.ValidationsMessage.SUBSCRIPTION_PLAN_NULL;
import static com.recurrence.constants.ValidationsMessage.SUBSCRIPTION_REFERENCE_ID_NULL;

public final class SubscrptionValidator {
    private SubscrptionValidator(){}

    public static void validateCreateRequest(Subscription request){
        if (request == null) {
            throw new RecurrenceValidationException(SUBSCRIPTION_NULL);
        }

        if (request.getReferenceId() == null || request.getReferenceId().isBlank()) {
            throw new RecurrenceValidationException(SUBSCRIPTION_REFERENCE_ID_NULL);
        }

        if (request.getPlan() == null) {
            throw new RecurrenceValidationException(SUBSCRIPTION_PLAN_NULL);
        }

        if (request.getCustomer() == null) {
            throw new RecurrenceValidationException(SUBSCRIPTION_CUSTOMER_NULL);
        }

        if (request.getPaymentMethod() == null || request.getPaymentMethod().isEmpty()) {
            throw new RecurrenceValidationException(SUBSCRIPTION_PAYMENT_METHOD_NULL);
        }
    }
}
