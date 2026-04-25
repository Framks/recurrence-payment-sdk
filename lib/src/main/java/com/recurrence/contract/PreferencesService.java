package com.recurrence.contract;

import com.recurrence.contract.domain.NotificationPreferences;
import com.recurrence.contract.domain.PublicKey;
import com.recurrence.contract.domain.RetryPreferences;
import com.recurrence.contract.infra.RequestResponse;

public interface PreferencesService {
    RequestResponse<PublicKey> getPublicKey() throws Exception;
    RequestResponse<PublicKey> putPublicKey() throws Exception;
    RequestResponse<NotificationPreferences> getNotificationPreferences() throws Exception;
    RequestResponse<NotificationPreferences> updateNotificationPreferences(NotificationPreferences notificationPreferences) throws Exception;
    RequestResponse<RetryPreferences> getRetryPreferences() throws Exception;
    RequestResponse<RetryPreferences> updateRetryPreferences(RetryPreferences retryPreferences) throws Exception;
}
