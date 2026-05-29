package com.recurrence.pagbank.domain.commons;

import org.jspecify.annotations.NonNull;

public record PagBankBillingInfo(
    String type,
    PagBankCard card,
    PagBankBoleto boleto
){}

