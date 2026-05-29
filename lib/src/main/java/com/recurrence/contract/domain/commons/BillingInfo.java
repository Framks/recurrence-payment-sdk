package com.recurrence.contract.domain.commons;

// Vale ajustar para ter um builder para criar?
public record BillingInfo(
    PaymentMethodType type,
    Card card,
    Boleto boleto
) { }
