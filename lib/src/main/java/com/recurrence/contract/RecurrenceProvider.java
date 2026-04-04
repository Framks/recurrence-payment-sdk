package com.recurrence.contract;

public interface RecurrenceProvider {
    PlanService planService();
    CouponService couponService();
    CustomerService customerService();
    SubscriptionService subscriptionService();
    InvoiceService invoiceService();

    // parte de chave publica fica implementado pelo provedor ou por outra interfaçe que vai cuidar de configuraçẽos ou preferencias ( nessa ultima engloba também as
    // configuraçẽos de notificação whebook, por exemplo)?
    // resposta:
}
