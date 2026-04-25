package com.recurrence.contract.domain.commons;

public record Boleto(
    String id,
    String barcode,
    String formattedBarcode,
    String dueAt,
    String pdfUrl,
    String imageUrl
) {
}
