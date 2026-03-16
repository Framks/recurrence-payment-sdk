package com.recurrence.pagbank.domain.commons;

public record Phone (
        int id,
        String area,
        String country,
        String number
){ }