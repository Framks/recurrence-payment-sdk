package com.recurrence.constants;

public final class ValidationsMessage {

    private ValidationsMessage(){}

    public static final String COUPON_NULL = "invalid request, coupon is required.";
    public static final String COUPON_NAME_NULL = "invalid request, name is required.";
    public static final String COUPON_DISCOUNT_NULL = "invalid request, discount is required.";
    public static final String COUPON_DISCOUNT_TYPE_NULL = "invalid request, Discount.type is required.";
    public static final String COUPON_DISCOUNT_VALUE_NULL = "invalid request, Discount.value is required.";
    public static final String COUPON_DISCOUNT_MINOR_ZERO = "invalid request, Discount.value has been more that 0.";
    public static final String COUPON_DURATION_NULL = "invalid request, duration is required.";
    public static final String COUPON_DURATION_TYPE_NULL = "invalid request, Duration.type is required.";
    public static final String COUPON_REDEMPTION_LIMIT_NULL = "invalid request, redemption_limit is required.";
    public static final String COUPON_REDEMPTION_LIMIT_ZERO = "invalid request, redemption_limit has been more that 0.";


    public static final String CUSTOMER_NULL = "invalid request, customer is required.";
    public static final String CUSTOMER_NAME_NULL = "invalid request, name is required.";
    public static final String CUSTOMER_EMAIL_NULL = "invalid request, email is required.";
    public static final String CUSTOMER_TAXID_NULL = "invalid request, taxId is required.";
    public static final String CUSTOMER_PHONE_NULL = "invalid request, phone must have at least one item.";


    public static final String PLAN_NULL = "invalid request, plan is required.";
    public static final String PLAN_NAME_NULL = "invalid request, name is required.";
    public static final String PLAN_AMOUNT_NULL = "invalid request, amount is required.";
    public static final String PLAN_AMOUNT_VALUE_NULL = "invalid request, Amount.value is required.";
    public static final String PLAN_AMOUNT_CURRENCY_NULL = "invalid request,  Amount.currency is required.";
    public static final String PLAN_AMOUNT_VALUE_ZERO = "invalid request,  Amount.value has been more that 0.";


    public static final String SUBSCRIPTION_NULL = "invalid request, subscription is required.";
    public static final String SUBSCRIPTION_REFERENCE_ID_NULL = "invalid request, referenceId is required.";
    public static final String SUBSCRIPTION_PLAN_NULL = "invalid request, plan is required.";
    public static final String SUBSCRIPTION_CUSTOMER_NULL = "invalid request, customer is required.";
    public static final String SUBSCRIPTION_PAYMENT_METHOD_NULL = "invalid request, payment_method must have at least one item.";
}
