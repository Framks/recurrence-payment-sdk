package com.recurrence.pagbank.validator;

import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.request.CreateSubscriptionRequest;
import com.recurrence.contract.domain.request.CustomerRequest;
import com.recurrence.exception.RecurrenceValidationException;

import static com.recurrence.constants.ValidationsMessage.CARD_SECURITY_CODE_NULL;
import static com.recurrence.constants.ValidationsMessage.CUSTOMER_NAME_NULL;
import static com.recurrence.constants.ValidationsMessage.CUSTOMER_PHONE_NULL;
import static com.recurrence.constants.ValidationsMessage.CUSTOMER_TAXID_NULL;
import static com.recurrence.constants.ValidationsMessage.SUBSCRIPTION_REFERENCE_ID_NULL;

/**
 * Regras exclusivas do PagBank para assinaturas, aplicadas depois do
 * {@code RequestValidator} comum.
 */
public final class SubscrptionValidator {

    private SubscrptionValidator() {}

    /** O PagBank cria o assinante junto com a assinatura e exige CPF, telefone e CVV. */
    public static void validateCreate(CreateSubscriptionRequest request) {
        if (isBlank(request.getReferenceId())) {
            throw new RecurrenceValidationException(SUBSCRIPTION_REFERENCE_ID_NULL);
        }
        CustomerRequest customer = request.getCustomer();
        if (isBlank(customer.getName())) throw new RecurrenceValidationException(CUSTOMER_NAME_NULL);
        if (isBlank(customer.getTaxId())) throw new RecurrenceValidationException(CUSTOMER_TAXID_NULL);
        if (customer.getPhone() == null) throw new RecurrenceValidationException(CUSTOMER_PHONE_NULL);
        validateSecurityCode(request.getPaymentMethod());
    }

    public static void validateSecurityCode(BillingInfo paymentMethod) {
        if (paymentMethod != null && isBlank(paymentMethod.getCard().getSecurityCode())) {
            throw new RecurrenceValidationException(CARD_SECURITY_CODE_NULL);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
