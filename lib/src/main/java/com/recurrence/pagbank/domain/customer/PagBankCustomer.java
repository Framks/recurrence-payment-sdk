package com.recurrence.pagbank.domain.customer;

import com.recurrence.pagbank.domain.commons.PagBankBillingInfo;
import com.recurrence.pagbank.domain.commons.PagBankPhone;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Assinante no formato do PagBank.
 *
 * <p>Na criação da assinatura vai com {@code id} nulo e os dados completos; nas
 * respostas o PagBank pode devolver só parte dos campos.</p>
 */
public record PagBankCustomer(
        String id,
        String name,
        String email,
        String taxId,
        List<PagBankPhone> phones,
        List<PagBankBillingInfo> billingInfo,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static PagBankCustomer newCustomer(
            String name,
            String email,
            String taxId,
            List<PagBankPhone> phones,
            List<PagBankBillingInfo> billingInfo
    ) {
        return new PagBankCustomer(null, name, email, taxId, phones, billingInfo, null, null);
    }
}
