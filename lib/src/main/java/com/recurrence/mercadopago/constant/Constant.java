package com.recurrence.mercadopago.constant;

public final class Constant {
    private Constant() {}

    public static final String BASE_URL = "https://api.mercadopago.com";

    public static final String PLANS = "/preapproval_plan";
    public static final String SUBSCRIPTIONS = "/preapproval";
    public static final String INVOICES = "/authorized_payments";
    public static final String PAYMENTS = "/v1/payments";
    public static final String CARD_TOKENS = "/v1/card_tokens";
    public static final String PATH_SEARCH = "/search";

    public static final String PLAN_ID = "planId";
    public static final String SUBSCRIPTION_ID = "subscriptionId";
    public static final String PAYMENT_ID = "paymentId";
    public static final String INVOICE_ID = "invoiceId";

    public static final String STATUS = "status";
    public static final String QUERY = "q";
    public static final String EXTERNAL_REFERENCE = "external_reference";
    public static final String PREAPPROVAL_ID = "preapproval_id";
    public static final String RANGE = "range";
    public static final String BEGIN_DATE = "begin_date";
    public static final String END_DATE = "end_date";
    public static final String SORT = "sort";
    public static final String CRITERIA = "criteria";
    public static final String DATE_CREATED = "date_created";
    public static final String DESC = "desc";

    /** Estados do Mercado Pago usados nas ações de plano e assinatura. */
    public static final class Status {
        private Status() {}

        public static final String PLAN_ACTIVE = "active";
        /** O sandbox converte {@code inactive} em {@code cancelled}; reativar com {@code active} funciona. */
        public static final String PLAN_CANCELLED = "cancelled";
        public static final String SUBSCRIPTION_AUTHORIZED = "authorized";
        public static final String SUBSCRIPTION_PAUSED = "paused";
        public static final String SUBSCRIPTION_CANCELLED = "cancelled";
    }

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
        public static final String SUBSCRIPTION_GET = "subscription.get";
        public static final String SUBSCRIPTION_SUSPEND = "subscription.suspend";
        public static final String SUBSCRIPTION_UPDATE = "subscription.update";
        public static final String SUBSCRIPTION_CANCEL = "subscription.cancel";
        public static final String SUBSCRIPTION_LIST = "subscription.list";
        public static final String SUBSCRIPTION_INVOICE_LIST = "subscription.invoices.list";
        public static final String INVOICE_LIST_PAYMENT = "invoice.payments.list";
        public static final String PAYMENT_LIST = "payment.list";
        public static final String INVOICE_GET = "invoice.get";
        public static final String PAYMENT_GET = "payment.get";
        public static final String CARD_TOKENIZE = "card.tokenize";
    }
}
