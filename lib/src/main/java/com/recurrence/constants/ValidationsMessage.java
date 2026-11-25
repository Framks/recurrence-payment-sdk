package com.recurrence.constants;

public final class ValidationsMessage {

    private ValidationsMessage(){}

    public static final String ID_BLANK = "invalid request, %s is required.";
    public static final String NOTHING_TO_UPDATE = "invalid request, at least one field must be informed to update.";

    public static final String AMOUNT_VALUE_NULL = "invalid request, Amount.value is required.";
    public static final String AMOUNT_CURRENCY_NULL = "invalid request, Amount.currency is required.";
    public static final String AMOUNT_VALUE_ZERO = "invalid request, Amount.value must be greater than 0.";

    public static final String PAYMENT_METHOD_NULL = "invalid request, paymentMethod is required.";
    public static final String PAYMENT_METHOD_TYPE_UNSUPPORTED = "invalid request, paymentMethod.type must be CREDIT_CARD.";
    public static final String CARD_TOKEN_NULL = "invalid request, paymentMethod.card.token is required.";
    public static final String CARD_SECURITY_CODE_NULL = "invalid request, paymentMethod.card.securityCode is required.";

    public static final String CARD_REQUEST_NULL = "invalid request, card is required.";
    public static final String CARD_NUMBER_INVALID = "invalid request, card.number must have only digits, between 13 and 19.";
    public static final String CARD_SECURITY_CODE_INVALID = "invalid request, card.securityCode must have 3 or 4 digits.";
    public static final String CARD_EXP_MONTH_INVALID = "invalid request, card.expMonth must be between 1 and 12.";
    public static final String CARD_EXP_YEAR_INVALID = "invalid request, card.expYear must be between 1900 and 2099.";
    public static final String CARD_HOLDER_NAME_INVALID = "invalid request, card.holderName is required and must not contain digits.";
    public static final String CARD_HOLDER_TAXID_INVALID = "invalid request, card.holderTaxId must have only digits, 11 (CPF) or 14 (CNPJ).";
    public static final String CARD_HOLDER_TAXID_NULL = "invalid request, card.holderTaxId is required by %s.";

    public static final String CUSTOMER_NULL = "invalid request, customer is required.";
    public static final String CUSTOMER_NAME_NULL = "invalid request, customer.name is required.";
    public static final String CUSTOMER_EMAIL_NULL = "invalid request, customer.email is required.";
    public static final String CUSTOMER_TAXID_NULL = "invalid request, customer.taxId is required.";
    public static final String CUSTOMER_PHONE_NULL = "invalid request, customer.phone is required.";

    public static final String PLAN_NULL = "invalid request, plan is required.";
    public static final String PLAN_NAME_NULL = "invalid request, name is required.";
    public static final String PLAN_AMOUNT_NULL = "invalid request, amount is required.";
    public static final String PLAN_INTERVAL_NULL = "invalid request, interval is required.";
    public static final String PLAN_INTERVAL_INVALID = "invalid request, interval.type is required and interval.frequency must be greater than 0.";
    public static final String PLAN_BILLING_CYCLES_INVALID = "invalid request, billingCycles must be greater than 0.";
    public static final String PLAN_TRIAL_INVALID = "invalid request, trial.days must be greater than 0.";
    public static final String PLAN_PAYMENT_METHOD_UNSUPPORTED = "invalid request, paymentMethods accepts only CREDIT_CARD.";

    public static final String SUBSCRIPTION_NULL = "invalid request, subscription is required.";
    public static final String SUBSCRIPTION_REFERENCE_ID_NULL = "invalid request, referenceId is required.";
    public static final String SUBSCRIPTION_PLAN_NULL = "invalid request, planId is required.";

    public static final String FILTER_STATUS_UNKNOWN = "invalid filter, status UNKNOWN cannot be used as filter.";
    public static final String FILTER_STATUS_UNSUPPORTED = "invalid filter, status %s is not supported by %s.";
    public static final String FILTER_DATE_PAIR_INVALID = "invalid filter, createdFrom and createdUntil must be informed together, with createdFrom before createdUntil.";
    public static final String FILTER_DATE_RANGE_INVALID = "invalid filter, createdFrom and createdUntil must be informed together, in order, with at most 90 days between them.";
}
