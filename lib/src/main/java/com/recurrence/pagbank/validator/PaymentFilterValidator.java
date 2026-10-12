package com.recurrence.pagbank.validator;

import com.recurrence.contract.filters.PaymentFilter;
import com.recurrence.exception.RecurrenceValidationException;

import java.time.temporal.ChronoUnit;

import static com.recurrence.constants.ValidationsMessage.FILTER_DATE_RANGE_INVALID;

/**
 * Regra do PagBank para o filtro de pagamentos: quando há filtro por data,
 * {@code created_at_start} e {@code created_at_end} são obrigatórios juntos e
 * o intervalo não pode passar de 90 dias (erro {@code date_filter_error}).
 */
public final class PaymentFilterValidator {

    private static final long MAX_RANGE_DAYS = 90;

    private PaymentFilterValidator() {}

    public static void validate(PaymentFilter filter) {
        boolean hasFrom = filter.getCreatedFrom() != null;
        boolean hasUntil = filter.getCreatedUntil() != null;
        if (!hasFrom && !hasUntil) {
            return;
        }
        if (hasFrom != hasUntil) {
            throw new RecurrenceValidationException(FILTER_DATE_RANGE_INVALID);
        }
        long days = ChronoUnit.DAYS.between(
                filter.getCreatedFrom().toLocalDate(),
                filter.getCreatedUntil().toLocalDate());
        if (days < 0 || days > MAX_RANGE_DAYS) {
            throw new RecurrenceValidationException(FILTER_DATE_RANGE_INVALID);
        }
    }
}
