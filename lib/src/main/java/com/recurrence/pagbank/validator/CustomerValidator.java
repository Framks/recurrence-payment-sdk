package com.recurrence.pagbank.validator;

import com.recurrence.contract.domain.Customer;
import com.recurrence.exception.RecurrenceValidationException;

import static com.recurrence.constants.ValidationsMessage.CUSTOMER_EMAIL_NULL;
import static com.recurrence.constants.ValidationsMessage.CUSTOMER_NAME_NULL;
import static com.recurrence.constants.ValidationsMessage.CUSTOMER_NULL;
import static com.recurrence.constants.ValidationsMessage.CUSTOMER_PHONE_NULL;
import static com.recurrence.constants.ValidationsMessage.CUSTOMER_TAXID_NULL;

public final class CustomerValidator {
    private CustomerValidator(){}

    public static void validateCreateRequest(Customer request){
        if (request == null) {
            throw new RecurrenceValidationException(CUSTOMER_NULL);
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new RecurrenceValidationException(CUSTOMER_NAME_NULL);
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RecurrenceValidationException(CUSTOMER_EMAIL_NULL);
        }

        if (request.getTaxId() == null || request.getTaxId().isBlank()) {
            throw new RecurrenceValidationException(CUSTOMER_TAXID_NULL);
        }

        if (request.getPhone() == null || request.getPhone().isEmpty()) {
            throw new RecurrenceValidationException(CUSTOMER_PHONE_NULL);
        }
    }
}
