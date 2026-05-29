package com.recurrence.contract.domain.commons;

public record Phone(
    Integer id,
    String area,
    String country,
    String number
) {
}
// seria bom mudar para uma classe e ter um build?