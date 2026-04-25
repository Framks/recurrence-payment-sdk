package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.NotificationPreferences;
import com.recurrence.contract.domain.PublicKey;
import com.recurrence.contract.domain.RetryPreferences;
import com.recurrence.pagbank.domain.commons.PagBankPreferencesNotification;
import com.recurrence.pagbank.domain.commons.PagBankPublicKey;
import com.recurrence.pagbank.domain.commons.PagBankReferencesRetry;
import java.util.List;

public final class PagBankPreferencesMapper {
    public static PublicKey toContract(PagBankPublicKey pagBankPublicKey) {
        if (pagBankPublicKey == null) return null;
        return new PublicKey(pagBankPublicKey.publicKey());
    }

    public static NotificationPreferences toContract(PagBankPreferencesNotification pagBankPreferences) {
        return NotificationPreferences.builder()
            .webhook(pagBankPreferences.getUrls().get(0))
            .emailCustomer(pagBankPreferences.getEmail().customer().enabled())
            .emailMerchant(pagBankPreferences.getEmail().merchant().enabled())
            .build();
    }

    public static PagBankPreferencesNotification toDomain(NotificationPreferences notificationPreferences) {
        return PagBankPreferencesNotification.builder()
            .webhooks(List.of(notificationPreferences.getWebhook()))
            .emailCustomer(notificationPreferences.getEmailCustomer())
            .emailMerchant(notificationPreferences.getEmailMerchant())
            .build();
    }

    public static PagBankReferencesRetry toDomain(RetryPreferences retryPreferences) {
        return PagBankReferencesRetry.builder()
            .firstTry(retryPreferences.getFirstTry())
            .secondTry(retryPreferences.getSecondTry())
            .thirdTry(retryPreferences.getThirdTry())
            .finallyStatus(retryPreferences.getFinallyStatus())
            .build();
    }

    public static RetryPreferences toContract(PagBankReferencesRetry pagBankReferencesRetry) {
        return RetryPreferences.builder()
            .firstTry(pagBankReferencesRetry.getFirstTry())
            .secondTry(pagBankReferencesRetry.getSecondTry())
            .thirdTry(pagBankReferencesRetry.getThirdTry())
            .finallyStatus(pagBankReferencesRetry.getFinallyStatus())
            .build();
    }
}
