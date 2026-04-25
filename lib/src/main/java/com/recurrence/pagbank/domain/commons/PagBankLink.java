package com.recurrence.pagbank.domain.commons;

public record PagBankLink(
    String rel,
    String href,
    String media,
    String type
) {}
