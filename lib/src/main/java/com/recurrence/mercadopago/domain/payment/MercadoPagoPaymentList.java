package com.recurrence.mercadopago.domain.payment;

import com.recurrence.mercadopago.domain.commons.MercadoPagoPaging;

import java.util.List;

public record MercadoPagoPaymentList(
        MercadoPagoPaging paging,
        List<MercadoPagoPayment> results
) {
}
