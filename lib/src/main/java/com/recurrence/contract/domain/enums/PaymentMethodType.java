package com.recurrence.contract.domain.enums;

public enum PaymentMethodType {
    CREDIT_CARD,
    /**
     * Meio de pagamento devolvido pelo provedor sem equivalente canônico
     * (ex.: boleto no PagBank). Não aceito em requisições.
     */
    UNKNOWN,
}
