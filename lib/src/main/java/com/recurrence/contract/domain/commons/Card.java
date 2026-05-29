package com.recurrence.contract.domain.commons;

// esse aqui com certeza deve ter um build pois é muitos campos apra ser passado tudo como null.
// e vale resaltar que tem mais de uma maneira de enviar esses dados.
public record Card(
    String number,
    String expYear,
    String expMonth,
    String encrypted,
    Holder holder,
    String token,
    String brand,
    String firstDigits,
    String lastDigits,
    String securityCode
) {
}
