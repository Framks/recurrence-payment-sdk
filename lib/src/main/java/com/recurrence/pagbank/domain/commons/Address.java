package com.recurrence.pagbank.domain.commons;

public record Address (
        String street,
        String number,
        String complement,
        String locality,
        String city,
        String regionCode,
        String country,
        String postalCode
){}
