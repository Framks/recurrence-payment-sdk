package com.recurrence.mercadopago.validator;

import com.recurrence.contract.filters.PaymentFilter;
import com.recurrence.exception.RecurrenceValidationException;

import static com.recurrence.constants.ValidationsMessage.FILTER_DATE_PAIR_INVALID;

/**
 * Regra do Mercado Pago para a busca de pagamentos: o período usa
 * {@code range=date_created} com {@code begin_date} e {@code end_date} juntos.
 */
public final class PaymentFilterValidator {

    private PaymentFilterValidator() {}

    public static void validate(PaymentFilter filter) {
        boolean hasFrom = filter.getCreatedFrom() != null;
        boolean hasUntil = filter.getCreatedUntil() != null;
        if (!hasFrom && !hasUntil) {
            return;
        }
        if (hasFrom != hasUntil || filter.getCreatedFrom().isAfter(filter.getCreatedUntil())) {
            throw new RecurrenceValidationException(FILTER_DATE_PAIR_INVALID);
        }
    }
}
