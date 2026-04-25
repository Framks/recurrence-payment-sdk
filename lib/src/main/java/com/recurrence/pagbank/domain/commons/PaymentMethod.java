package com.recurrence.pagbank.domain.commons;

public record PaymentMethod(
    String type,
    PagBankCard card,
    PagBankBoleto boleto
){}
