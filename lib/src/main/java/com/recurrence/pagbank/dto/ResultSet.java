package com.recurrence.pagbank.dto;

public record ResultSet(
       int offset,
       int limit,
       int total
) { }
