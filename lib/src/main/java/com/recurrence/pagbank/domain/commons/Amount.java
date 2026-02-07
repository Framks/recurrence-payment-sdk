package com.recurrence.pagbank.domain.commons;

public record Amount(
        int value,
        String currency
) {
    public Amount(int value) {
        this(value, "BRL");
    }
}
