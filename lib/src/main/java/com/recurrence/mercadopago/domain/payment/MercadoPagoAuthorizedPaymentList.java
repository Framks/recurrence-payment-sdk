package com.recurrence.mercadopago.domain.payment;

import com.recurrence.mercadopago.domain.commons.MercadoPagoPaging;

import java.util.List;

public record MercadoPagoAuthorizedPaymentList(
        MercadoPagoPaging paging,
        List<MercadoPagoAuthorizedPayment> results
) {
}
