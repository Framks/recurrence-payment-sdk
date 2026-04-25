package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.commons.Address;
import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.commons.Holder;
import com.recurrence.contract.domain.commons.Phone;
import com.recurrence.pagbank.domain.commons.PagBankAmount;
import com.recurrence.pagbank.domain.commons.PagBankBillingInfo;
import com.recurrence.pagbank.domain.commons.PagBankCard;
import com.recurrence.pagbank.domain.commons.PaymentMethod;

public class CommonsMapper {

    private CommonsMapper(){ }

    public static Address toContractAddress(com.recurrence.pagbank.domain.commons.Address address){
        return new Address(
                address.street(),
                address.number(),
                address.complement(),
                address.locality(),
                address.city(),
                address.regionCode(),
                address.country(),
                address.postalCode()
        );
    }

    public static com.recurrence.pagbank.domain.commons.Address toPagBankAddress(Address address){
        if (address == null) return null;

        return new com.recurrence.pagbank.domain.commons.Address(
                address.street(),
                address.number(),
                address.complement(),
                address.locality(),
                address.city(),
                address.regionCode(),
                address.country(),
                address.postalCode()
        );
    }

    public static PagBankAmount toPagBankAmount(com.recurrence.contract.domain.commons.Amount amount){
        return new PagBankAmount(
                amount.value(),
                amount.currency()
        );
    }

    public static Amount toContractAmount(PagBankAmount pagBankAmount){
        return new Amount(
                pagBankAmount.value(),
                pagBankAmount.currency()
        );
    }

    public static BillingInfo toContractBillingInfo(PagBankBillingInfo pagBankBillingInfo){
        if (pagBankBillingInfo == null) return null;

        if ( pagBankBillingInfo.card() != null){
            return new BillingInfo(
                pagBankBillingInfo.type(),
                new Card(
                    pagBankBillingInfo.card().number(),
                    pagBankBillingInfo.card().expYear(),
                    pagBankBillingInfo.card().expMonth(),
                    pagBankBillingInfo.card().encrypted(),
                    new Holder(
                        pagBankBillingInfo.card().holder().name()
                    ),
                    pagBankBillingInfo.card().token(),
                    pagBankBillingInfo.card().brand(),
                    pagBankBillingInfo.card().firstDigits(),
                    pagBankBillingInfo.card().lastDigits(),
                    null
                ),
                null
            );
        }

        return new BillingInfo(
            pagBankBillingInfo.type(),
            null,
            null
        );
    }

    public static BillingInfo toContractBillingInfo(PaymentMethod paymentMethod){
        if (paymentMethod == null) return null;

        if ( paymentMethod.card() != null){
            return new BillingInfo(
                paymentMethod.type(),
                new Card(
                    paymentMethod.card().number(),
                    paymentMethod.card().expYear(),
                    paymentMethod.card().expMonth(),
                    paymentMethod.card().encrypted(),
                    new Holder(
                        paymentMethod.card().holder().name()
                    ),
                    paymentMethod.card().token(),
                    paymentMethod.card().brand(),
                    paymentMethod.card().firstDigits(),
                    paymentMethod.card().lastDigits(),
                    null
                ),
                null
            );
        }

        return new BillingInfo(
            paymentMethod.type(),
            null,
            null
        );
    }

    public static PagBankBillingInfo toPagBankBillingInfo(BillingInfo billingInfo){
        if (billingInfo == null) return null;

        if ( billingInfo.card() != null){
            return new PagBankBillingInfo(
                billingInfo.type(),
                new PagBankCard(
                    billingInfo.card().token(),
                    billingInfo.card().number(),
                    billingInfo.card().encrypted(),
                    billingInfo.card().brand(),
                    billingInfo.card().firstDigits(),
                    billingInfo.card().lastDigits(),
                    billingInfo.card().expMonth(),
                    billingInfo.card().expYear(),
                    new PagBankCard.Holder(
                        billingInfo.card().holder().name(),
                        null,
                        null
                    ),
                    null
                ),
                null
            );
        }

        return new PagBankBillingInfo(
            billingInfo.type(),
            null,
            null
        );
    }

    public static Phone toContractPhone(com.recurrence.pagbank.domain.commons.Phone phone){
        return new Phone(
            phone.id(),
            phone.area(),
            phone.country(),
            phone.number()
        );
    }

    public static com.recurrence.pagbank.domain.commons.Phone toPagBankPhone(Phone phone){
        return new com.recurrence.pagbank.domain.commons.Phone(
            phone.id(),
            phone.area(),
            phone.country(),
            phone.number()
        );
    }
}
