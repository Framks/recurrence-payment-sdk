package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.commons.Address;
import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.commons.Holder;
import com.recurrence.contract.domain.commons.Phone;

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

    public static com.recurrence.pagbank.domain.commons.Amount toPagBankAmount(com.recurrence.contract.domain.commons.Amount amount){
        return new com.recurrence.pagbank.domain.commons.Amount(
                amount.value(),
                amount.currency()
        );
    }

    public static Amount toContractAmount(com.recurrence.pagbank.domain.commons.Amount amount){
        return new Amount(
                amount.value(),
                amount.currency()
        );
    }

    public static BillingInfo toContractBillingInfo(com.recurrence.pagbank.domain.commons.BillingInfo billingInfo){
        if (billingInfo == null) return null;

        if ( billingInfo.card() != null){
            return new BillingInfo(
                billingInfo.type(),
                new Card(
                    billingInfo.card().number(),
                    billingInfo.card().expYear(),
                    billingInfo.card().expMonth(),
                    billingInfo.card().encrypted(),
                    new Holder(
                        billingInfo.card().holder().name()
                    ),
                    billingInfo.card().token(),
                    billingInfo.card().brand(),
                    billingInfo.card().firstDigits(),
                    billingInfo.card().lastDigits()
                )
            );
        }

        return new BillingInfo(
            billingInfo.type(),
            null
        );
    }

    public static com.recurrence.pagbank.domain.commons.BillingInfo toPagBankBillingInfo(BillingInfo billingInfo){
        if (billingInfo == null) return null;

        if ( billingInfo.card() != null){
            return new com.recurrence.pagbank.domain.commons.BillingInfo(
                billingInfo.type(),
                new com.recurrence.pagbank.domain.commons.Card(
                    billingInfo.card().token(),
                    billingInfo.card().number(),
                    billingInfo.card().encrypted(),
                    billingInfo.card().brand(),
                    billingInfo.card().firstDigits(),
                    billingInfo.card().lastDigits(),
                    billingInfo.card().expMonth(),
                    billingInfo.card().expYear(),
                    new com.recurrence.pagbank.domain.commons.Card.Holder(
                        billingInfo.card().holder().name(),
                        null,
                        null
                    )
                )
            );
        }

        return new com.recurrence.pagbank.domain.commons.BillingInfo(
            billingInfo.type(),
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
