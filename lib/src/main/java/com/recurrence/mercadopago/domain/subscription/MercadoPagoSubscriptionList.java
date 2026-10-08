package com.recurrence.mercadopago.domain.subscription;

import com.recurrence.mercadopago.domain.commons.MercadoPagoPaging;

import java.util.List;

public record MercadoPagoSubscriptionList(
        MercadoPagoPaging paging,
        List<MercadoPagoSubscription> results
) {
}
