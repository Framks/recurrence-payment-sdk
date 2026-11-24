package com.recurrence.pagbank.constant;

public final class Constant {
    private  Constant() {}


    public static final String PLAN_ID = "planId";
    public static final String SUBSCRIPTION_ID = "subscriptionId";
    public static final String PAYMENT_ID = "paymentId";
    public static final String INVOICE_ID = "invoiceId";
    public static final String PATH_ACTIVE = "/activate";
    public static final String PATH_INACTIVE = "/inactivate";
    public static final String PATH_CANCEL = "/cancel";
    public static final String PATH_SUSPEND = "/suspend";
    public static final String PATH_BILLING_INFO = "/billing_info";
    public static final String PATH_PUBLIC_KEYS = "/public-keys";
    public static final String STATUS = "status";
    public static final String REFERENCE_ID = "reference_id";
    public static final String CREATE_AT_START = "created_at_start";
    public static final String CREATE_AT_END = "created_at_end";

    public static final class Operation {
        private Operation() {}

        public static final String PLAN_CREATE = "plan.create";
        public static final String PLAN_LIST = "plan.list";
        public static final String PLAN_UPDATE = "plan.update";
        public static final String PLAN_ACTIVATE = "plan.activate";
        public static final String PLAN_GET = "plan.get";
        public static final String PLAN_DEACTIVATE = "plan.deactivate";
        public static final String SUBSCRIPTION_ACTIVATE = "subscription.activate";
        public static final String SUBSCRIPTION_CREATE = "subscription.create";
        public static final String SUBSCRIPTION_SUSPEND =  "subscription.suspend";
        public static final String SUBSCRIPTION_UPDATE = "subscription.update";
        public static final String SUBSCRIPTION_UPDATE_BILLING = "subscription.update.billing_info";
        public static final String SUBSCRIPTION_CANCEL = "subscription.cancel";
        public static final String SUBSCRIPTION_LIST = "subscription.list";
        public static final String SUBSCRIPTION_INVOICE_LIST = "subscription.invoices.list";
        public static final String INVOICE_LIST_PAYMENT = "invoice.payments.list";
        public static final String PAYMENT_LIST = "payment.list";
        public static final String INVOICE_GET = "invoice.get";
        public static final String PAYMENT_GET = "payment.get";
        public static final String CARD_PUBLIC_KEY = "card.public_key";
        public static final String CARD_ENCRYPT = "card.encrypt";
    }
}


