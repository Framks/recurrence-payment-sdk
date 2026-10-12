package com.recurrence.mercadopago.domain.plan;

import com.recurrence.mercadopago.domain.commons.MercadoPagoPaging;

import java.util.List;

public record MercadoPagoPlanList(
        MercadoPagoPaging paging,
        List<MercadoPagoPlan> results
) {
}
