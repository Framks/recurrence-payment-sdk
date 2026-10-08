package com.recurrence.mercadopago.domain.commons;

/** Paginação das buscas do Mercado Pago ({@code paging}). */
public record MercadoPagoPaging(
        Integer offset,
        Integer limit,
        Integer total
) {
}
