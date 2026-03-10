package com.recurrence.contract.domain.commons;

public record Card(
    String number,
    String expYear,
    String expMonth,
    String encrypted,
    Holder holder,
    String token,
    String brand,
    String firstDigits,
    String lastDigits
) {
}
