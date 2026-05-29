package com.recurrence.pagbank.domain.commons;

public record Phone (
        Integer id,
        String area,
        String country,
        String number
){ }