package com.recurrence.contract.domain.enums;

public enum SubscriptionStatus {
    ACTIVE,
    CANCELED,
    OVERDUE,
    PENDING,
    SUSPENDED,
    EXPIRED,
    /** Estado do provedor sem equivalente canônico. Não aceito em filtros. */
    UNKNOWN,
}
