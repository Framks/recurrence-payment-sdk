package com.recurrence.pagbank.domain.error;

import java.util.List;

public record PagBankErrorList(
    List<PagBankErrorDetail> errors
) {
}