package com.recurrence.contract;

import com.recurrence.contract.domain.NotificationPreferences;
import com.recurrence.contract.domain.PublicKey;
import com.recurrence.contract.domain.RetryPreferences;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.exception.RecurrenceException;

public interface PreferencesService {
    RequestResponse<PublicKey> getPublicKey() throws RecurrenceException;
    RequestResponse<PublicKey> putPublicKey() throws RecurrenceException;
    RequestResponse<NotificationPreferences> getNotificationPreferences() throws RecurrenceException;
    RequestResponse<NotificationPreferences> updateNotificationPreferences(NotificationPreferences notificationPreferences) throws RecurrenceException;
    RequestResponse<RetryPreferences> getRetryPreferences() throws RecurrenceException;
    RequestResponse<RetryPreferences> updateRetryPreferences(RetryPreferences retryPreferences) throws RecurrenceException;
    RequestResponse<RetryPreferences> updateRetryPreferences(RetryPreferences retryPreferences, RequestOptions requestOptions) throws RecurrenceException;
}
