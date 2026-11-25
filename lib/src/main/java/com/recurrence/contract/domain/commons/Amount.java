package com.recurrence.contract.domain.commons;

import com.recurrence.contract.domain.enums.Currency;

public record Amount(
    Integer value,
    Currency currency
) {
}
