package com.recurrence.mercadopago.domain.error;

import java.util.List;

/**
 * Corpo de erro do Mercado Pago. Exemplos observados no sandbox:
 * {@code {"message":"Back url is required","status":400}} e
 * {@code {"error":"resource not found","message":"..."}}; algumas rotas trazem {@code cause}.
 */
public record MercadoPagoError(
        String message,
        String error,
        String code,
        Integer status,
        List<Cause> cause
) {
    public record Cause(
            String code,
            String description
    ) {
    }
}
