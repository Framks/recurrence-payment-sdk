package com.recurrence.pagbank.domain.commons;

/**
 * Cartão no formato do PagBank.
 *
 * <p>Na requisição, {@code encrypted} (cartão criptografado pelo SDK JS do
 * PagBank) e {@code securityCode}; na resposta, os dados de exibição.</p>
 */
public record PagBankCard(
        String encrypted,
        String securityCode,
        String brand,
        String firstDigits,
        String lastDigits,
        String expMonth,
        String expYear
) {
    public static PagBankCard encrypted(String encrypted, String securityCode) {
        return new PagBankCard(encrypted, securityCode, null, null, null, null, null);
    }

    public static PagBankCard securityCode(String securityCode) {
        return new PagBankCard(null, securityCode, null, null, null, null, null);
    }
}
