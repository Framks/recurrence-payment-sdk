package com.recurrence.pagbank.validator;

import com.recurrence.contract.domain.request.CreatePlanRequest;
import com.recurrence.exception.RecurrenceValidationException;

import static com.recurrence.constants.ValidationsMessage.AMOUNT_CURRENCY_NULL;
import static com.recurrence.constants.ValidationsMessage.PLAN_AMOUNT_NULL;
import static com.recurrence.constants.ValidationsMessage.AMOUNT_VALUE_NULL;
import static com.recurrence.constants.ValidationsMessage.AMOUNT_VALUE_ZERO;
import static com.recurrence.constants.ValidationsMessage.PLAN_NAME_NULL;
import static com.recurrence.constants.ValidationsMessage.PLAN_NULL;

public final class PlanValidator {
    public static void validateCreateRequest(CreatePlanRequest request){
        if (request == null){
            throw new RecurrenceValidationException(PLAN_NULL);
        }

        if (request.getName().isBlank()) {
            throw new RecurrenceValidationException(PLAN_NAME_NULL);
        }

        if (request.getAmount() == null){
            throw new RecurrenceValidationException(PLAN_AMOUNT_NULL);
        }

        if (request.getAmount().value() == null){
            throw new RecurrenceValidationException(AMOUNT_VALUE_NULL);
        }

        if (request.getAmount().currency() == null){
            throw new RecurrenceValidationException(AMOUNT_CURRENCY_NULL);
        }

        if (request.getAmount().value() <= 0){
            throw new RecurrenceValidationException(AMOUNT_VALUE_ZERO);
        }
    }
}
