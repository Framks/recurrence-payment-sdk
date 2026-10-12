package com.recurrence.pagbank.domain.error;

public record PagBankErrorDetail(
        String error,
        String parameter_name,
        String description
) {
}