package com.recurrence.mercadopago.domain.commons;

import java.math.BigDecimal;

/**
 * Bloco {@code auto_recurring} do Mercado Pago, usado em planos e assinaturas.
 * Valores monetários são decimais (ex.: {@code 50.50}), não centavos.
 *
 * <p>{@code startDate}/{@code endDate} ficam como texto e são convertidos no mapper.</p>
 */
public record MercadoPagoAutoRecurring(
        Integer frequency,
        String frequencyType,
        Integer repetitions,
        BigDecimal transactionAmount,
        String currencyId,
        MercadoPagoFreeTrial freeTrial,
        String startDate,
        String endDate
) {
    public static MercadoPagoAutoRecurring of(
            Integer frequency,
            String frequencyType,
            Integer repetitions,
            BigDecimal transactionAmount,
            String currencyId,
            MercadoPagoFreeTrial freeTrial
    ) {
        return new MercadoPagoAutoRecurring(
                frequency, frequencyType, repetitions, transactionAmount, currencyId, freeTrial, null, null);
    }

    public boolean isEmpty() {
        return frequency == null && frequencyType == null && repetitions == null
                && transactionAmount == null && currencyId == null && freeTrial == null;
    }
}
