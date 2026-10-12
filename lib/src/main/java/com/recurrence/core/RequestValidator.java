package com.recurrence.core;

import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.PlanInterval;
import com.recurrence.contract.domain.enums.PaymentMethodType;
import com.recurrence.contract.domain.request.CreatePlanRequest;
import com.recurrence.contract.domain.request.CreateSubscriptionRequest;
import com.recurrence.contract.domain.request.TokenizeCardRequest;
import com.recurrence.contract.domain.request.UpdatePlanRequest;
import com.recurrence.contract.domain.request.UpdateSubscriptionRequest;
import com.recurrence.exception.RecurrenceValidationException;

import java.util.List;

import static com.recurrence.constants.ValidationsMessage.*;

/**
 * Regras de validação comuns a todos os provedores, aplicadas antes de
 * qualquer chamada HTTP.
 *
 * <p>Regras exclusivas de um provedor (ex.: PagBank exige CPF e telefone do
 * assinante) ficam no validator do próprio provedor.</p>
 */
public final class RequestValidator {

    private RequestValidator() {}

    public static void requireId(String value, String fieldName) {
        if (isBlank(value)) {
            throw new RecurrenceValidationException(String.format(ID_BLANK, fieldName));
        }
    }

    public static void validate(CreatePlanRequest request) {
        if (request == null) throw new RecurrenceValidationException(PLAN_NULL);
        if (isBlank(request.getName())) throw new RecurrenceValidationException(PLAN_NAME_NULL);
        if (request.getAmount() == null) throw new RecurrenceValidationException(PLAN_AMOUNT_NULL);
        validateAmount(request.getAmount());
        if (request.getInterval() == null) throw new RecurrenceValidationException(PLAN_INTERVAL_NULL);
        validateInterval(request.getInterval());
        validateBillingCycles(request.getBillingCycles());
        validateTrialDays(request.getTrial() == null ? null : request.getTrial().days());
        validatePlanPaymentMethods(request.getPaymentMethods());
    }

    public static void validate(UpdatePlanRequest request) {
        if (request == null) throw new RecurrenceValidationException(PLAN_NULL);
        if (request.getName() == null
                && request.getAmount() == null
                && request.getInterval() == null
                && request.getBillingCycles() == null
                && request.getTrial() == null
                && request.getPaymentMethods() == null) {
            throw new RecurrenceValidationException(NOTHING_TO_UPDATE);
        }
        if (request.getName() != null && request.getName().isBlank()) {
            throw new RecurrenceValidationException(PLAN_NAME_NULL);
        }
        if (request.getAmount() != null) validateAmount(request.getAmount());
        if (request.getInterval() != null) validateInterval(request.getInterval());
        validateBillingCycles(request.getBillingCycles());
        validateTrialDays(request.getTrial() == null ? null : request.getTrial().days());
        validatePlanPaymentMethods(request.getPaymentMethods());
    }

    public static void validate(CreateSubscriptionRequest request) {
        if (request == null) throw new RecurrenceValidationException(SUBSCRIPTION_NULL);
        requireId(request.getPlanId(), "planId");
        if (request.getAmount() != null) validateAmount(request.getAmount());
        if (request.getCustomer() == null) throw new RecurrenceValidationException(CUSTOMER_NULL);
        if (isBlank(request.getCustomer().getEmail())) throw new RecurrenceValidationException(CUSTOMER_EMAIL_NULL);
        validatePaymentMethod(request.getPaymentMethod());
    }

    public static void validate(UpdateSubscriptionRequest request) {
        if (request == null) throw new RecurrenceValidationException(SUBSCRIPTION_NULL);
        if (request.getPlanId() == null && request.getAmount() == null && request.getPaymentMethod() == null) {
            throw new RecurrenceValidationException(NOTHING_TO_UPDATE);
        }
        if (request.getPlanId() != null) requireId(request.getPlanId(), "planId");
        if (request.getAmount() != null) validateAmount(request.getAmount());
        if (request.getPaymentMethod() != null) validatePaymentMethod(request.getPaymentMethod());
    }

    /** Regras do SDK JS do PagBank, que também valem para o Mercado Pago. */
    public static void validate(TokenizeCardRequest request) {
        if (request == null) throw new RecurrenceValidationException(CARD_REQUEST_NULL);
        if (!isDigits(request.getNumber(), 13, 19)) throw new RecurrenceValidationException(CARD_NUMBER_INVALID);
        if (!isDigits(request.getSecurityCode(), 3, 4)) throw new RecurrenceValidationException(CARD_SECURITY_CODE_INVALID);
        if (request.getExpMonth() == null || request.getExpMonth() < 1 || request.getExpMonth() > 12) {
            throw new RecurrenceValidationException(CARD_EXP_MONTH_INVALID);
        }
        if (request.getExpYear() == null || request.getExpYear() < 1900 || request.getExpYear() > 2099) {
            throw new RecurrenceValidationException(CARD_EXP_YEAR_INVALID);
        }
        if (isBlank(request.getHolderName()) || request.getHolderName().chars().anyMatch(Character::isDigit)) {
            throw new RecurrenceValidationException(CARD_HOLDER_NAME_INVALID);
        }
        String taxId = request.getHolderTaxId();
        if (taxId != null && !(isDigits(taxId, 11, 11) || isDigits(taxId, 14, 14))) {
            throw new RecurrenceValidationException(CARD_HOLDER_TAXID_INVALID);
        }
    }

    private static void validateAmount(Amount amount) {
        if (amount.value() == null) throw new RecurrenceValidationException(AMOUNT_VALUE_NULL);
        if (amount.currency() == null) throw new RecurrenceValidationException(AMOUNT_CURRENCY_NULL);
        if (amount.value() <= 0) throw new RecurrenceValidationException(AMOUNT_VALUE_ZERO);
    }

    private static void validateInterval(PlanInterval interval) {
        if (interval.type() == null || interval.frequency() == null || interval.frequency() <= 0) {
            throw new RecurrenceValidationException(PLAN_INTERVAL_INVALID);
        }
    }

    private static void validateBillingCycles(Integer billingCycles) {
        if (billingCycles != null && billingCycles <= 0) {
            throw new RecurrenceValidationException(PLAN_BILLING_CYCLES_INVALID);
        }
    }

    private static void validateTrialDays(Integer days) {
        if (days != null && days <= 0) {
            throw new RecurrenceValidationException(PLAN_TRIAL_INVALID);
        }
    }

    private static void validatePlanPaymentMethods(List<PaymentMethodType> paymentMethods) {
        if (paymentMethods != null && paymentMethods.stream().anyMatch(type -> type != PaymentMethodType.CREDIT_CARD)) {
            throw new RecurrenceValidationException(PLAN_PAYMENT_METHOD_UNSUPPORTED);
        }
    }

    private static void validatePaymentMethod(BillingInfo paymentMethod) {
        if (paymentMethod == null) throw new RecurrenceValidationException(PAYMENT_METHOD_NULL);
        if (paymentMethod.getType() != PaymentMethodType.CREDIT_CARD) {
            throw new RecurrenceValidationException(PAYMENT_METHOD_TYPE_UNSUPPORTED);
        }
        if (paymentMethod.getCard() == null || isBlank(paymentMethod.getCard().getToken())) {
            throw new RecurrenceValidationException(CARD_TOKEN_NULL);
        }
    }

    private static boolean isDigits(String value, int minLength, int maxLength) {
        return value != null
                && value.length() >= minLength
                && value.length() <= maxLength
                && value.chars().allMatch(c -> c >= '0' && c <= '9');
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
