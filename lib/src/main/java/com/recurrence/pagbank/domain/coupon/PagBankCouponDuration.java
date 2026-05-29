package com.recurrence.pagbank.domain.coupon;

public record PagBankCouponDuration(     /// Todo fazer um mecanismo para desconciderar o occurrences quando o type não for igual REPEATING
        String type,
        Integer occurrences
) { }