package com.recurrence.contract.domain.enums;

public enum PaymentStatus {
    PENDING,
    APPROVED,
    UNPAID,
    DECLINED,
    REFUNDED,
    /** Estado do provedor sem equivalente canônico. Não aceito em filtros. */
    UNKNOWN,
}
