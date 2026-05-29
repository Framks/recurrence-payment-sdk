package com.recurrence.pagbank.validator;

import com.recurrence.contract.domain.Customer;
import com.recurrence.exception.RecurrenceValidationException;

public final class CustomerValidator {
    private CustomerValidator(){}

    public static void validateCreateRequest(Customer request){
        if (request == null) {
            throw new RecurrenceValidationException("invalid request, customer is required.");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new RecurrenceValidationException("invalid request, name is required.");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RecurrenceValidationException("invalid request, email is required.");
        }

        if (request.getTaxId() == null || request.getTaxId().isBlank()) {
            throw new RecurrenceValidationException("invalid request, taxId is required.");
        }

        if (request.getPhone() == null || request.getPhone().isEmpty()) {
            throw new RecurrenceValidationException("invalid request, phone must have at least one item.");
        }
    }
}
