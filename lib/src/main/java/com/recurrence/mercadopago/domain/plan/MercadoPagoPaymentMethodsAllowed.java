package com.recurrence.mercadopago.domain.plan;

import com.recurrence.mercadopago.domain.commons.MercadoPagoIdRef;

import java.util.List;

/** {@code payment_methods_allowed} do plano; o contrato usa só {@code payment_types}. */
public record MercadoPagoPaymentMethodsAllowed(
        List<MercadoPagoIdRef> paymentTypes
) {
}
