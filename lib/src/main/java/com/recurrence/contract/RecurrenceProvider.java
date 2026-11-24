package com.recurrence.contract;

import com.recurrence.exception.RecurrenceException;

public interface RecurrenceProvider {
    PlanService planService() throws RecurrenceException;
    SubscriptionService subscriptionService() throws RecurrenceException;
    InvoiceService invoiceService() throws RecurrenceException;
    CardService cardService() throws RecurrenceException;
}