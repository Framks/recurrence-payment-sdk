package com.recurrence.pagbank.domain.commons;

public record Card (
    String token,
    String number,
    String encrypted,
    String brand,
    String firstDigits,
    String lastDigits,
    String expMonth,
    String expYear,
    Holder holder
){
    public record Holder (
        String name,
        String taxId,
        String birthDate
    ){}
}
