package com.recurrence.pagbank.validator;

import com.recurrence.contract.domain.Plan;
import com.recurrence.exception.RecurrenceValidationException;

public final class PlanValidator {
    public static void validateCreateRequest(Plan request){
        if (request == null){
            throw new RecurrenceValidationException("invalid request, plan is required.");
        }

        if (request.getName().isBlank()) {
            throw new RecurrenceValidationException("invalid request, name is required.");
        }

        if (request.getAmount() == null){
            throw new RecurrenceValidationException("invalid request, amount is required.");
        }

        if (request.getAmount().value() == null){
            throw new RecurrenceValidationException("invalid request, Amount.value is required.");
        }

        if (request.getAmount().currency().isBlank()){
            throw new RecurrenceValidationException("invalid request,  Amount.currency is required.");
        }

        if (request.getAmount().value() <= 0){
            throw new RecurrenceValidationException("invalid request,  Amount.value has been more that 0.");
        }

        // devemos validar o codigo da moeda?
        // hoje o pagbank só aceita o BRL mas se começar a aceitar outras o cliente vai precisar
        // atualizar a lib só por calsa dessa verificação. vale a pena?
    }
}
