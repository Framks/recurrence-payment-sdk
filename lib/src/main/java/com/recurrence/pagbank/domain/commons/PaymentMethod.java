package com.recurrence.pagbank.domain.commons;

public record PaymentMethod(
    String type,
    Card card
){}
