package com.recurrence.pagbank.domain.commons;

/** Valor em centavos. */
public record PagBankAmount(
        Integer value,
        String currency
) {
}
