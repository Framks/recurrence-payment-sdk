package com.recurrence.contract;

import com.recurrence.contract.domain.NotificationPreferences;
import com.recurrence.contract.domain.PublicKey;
import com.recurrence.contract.domain.RetryPreferences;
import com.recurrence.contract.infra.RequestResponse;

public interface PreferencesService {
    RequestResponse<PublicKey> getPublicKey();
    RequestResponse<PublicKey> putPublicKey();
    RequestResponse<NotificationPreferences> getNotificationPreferences();
    RequestResponse<NotificationPreferences> updateNotificationPreferences(NotificationPreferences notificationPreferences);
    RequestResponse<RetryPreferences> getRetryPreferences();
    RequestResponse<RetryPreferences> updateRetryPreferences(RetryPreferences retryPreferences);
}
