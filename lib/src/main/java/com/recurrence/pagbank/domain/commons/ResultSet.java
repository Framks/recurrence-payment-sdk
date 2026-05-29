package com.recurrence.pagbank.domain.commons;

public record ResultSet(
       Integer offset,
       Integer limit,
       Integer total
) { }
