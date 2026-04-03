package com.recurrence.contract.domain.commons;

// Vale ajustar para ter um builder para criar?
public record BillingInfo(
    String type,
    Card card
) {

}
