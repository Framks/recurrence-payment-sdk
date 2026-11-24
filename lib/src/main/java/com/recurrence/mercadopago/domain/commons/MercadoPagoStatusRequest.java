package com.recurrence.mercadopago.domain.commons;

/**
 * Corpo usado para mudar o estado de plano ou assinatura: o Mercado Pago não tem
 * endpoints de ação, o estado é alterado pelo {@code PUT} geral do recurso.
 */
public record MercadoPagoStatusRequest(
        String status
) {
}
