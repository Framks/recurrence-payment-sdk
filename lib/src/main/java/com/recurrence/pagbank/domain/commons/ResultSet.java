package com.recurrence.pagbank.domain.commons;

public record ResultSet(
       int offset,
       int limit,
       int total
) { }
