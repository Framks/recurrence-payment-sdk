package com.recurrence.pagbank.domain.commons;

public record PagBankBoleto(
    String id,
    String barcode,
    String formattedBarcode,
    String dueAt
) {

}