package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.Customer;
import com.recurrence.pagbank.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.commons.Holder;
import com.recurrence.contract.domain.infra.PageResponse;
import com.recurrence.pagbank.domain.commons.Address;
import com.recurrence.pagbank.domain.commons.Phone;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import com.recurrence.pagbank.domain.customer.PagBankListCustomer;
import java.util.List;

public class PagbankCustomerMapper {

    private PagbankCustomerMapper(){
    }

    public static PageResponse<Customer> toPagResponse(PagBankListCustomer pagBankListCustomer){
        return new PageResponse<>(
            new PageResponse.PageInfo(
                pagBankListCustomer.getResultSet().offset(),
                pagBankListCustomer.getResultSet().limit(),
                pagBankListCustomer.getResultSet().total()
            ),
            pagBankListCustomer.getCustomers().stream().map(PagbankCustomerMapper::to).toList()
        );
    }

    public static Customer to(PagBankCustomer customer){
        var billingInfo = customer.getBillingInfo();
        return Customer.builder()
            .id(customer.getId())
            .referenceId(customer.getReferenceId())
            .name(customer.getName())
            .email(customer.getEmail())
            .taxId(customer.getTaxId())
            //.phone()
            .billingInfo(new com.recurrence.contract.domain.commons.BillingInfo(
                billingInfo.type(),
                new Card(
                    billingInfo.card().number(),
                    billingInfo.card().expYear(),
                    billingInfo.card().expMonth(),
                    billingInfo.card().encrypted(),
                    new Holder(billingInfo.card().holder().name()),
                    billingInfo.card().token(),
                    billingInfo.card().brand(),
                    billingInfo.card().firstDigits(),
                    billingInfo.card().lastDigits()
                )
            ))
            .birthDate(customer.getBirthDate())
                    
            .build();
    }

    public static PagBankCustomer toUpdate(Customer customer){
        return PagBankCustomer.builder()
            .referenceId(customer.getReferenceId())
            .name(customer.getName())
            .email(customer.getEmail())
            .phone(makePhone(customer.getPhone()))
            .birthDate(customer.getBirthDate())
            .address(makeAddress(customer.getAddress()))
            .build();
    }

    public static PagBankCustomer from(Customer customer){
        return PagBankCustomer.builder()
            .referenceId(customer.getReferenceId())
            .name(customer.getName())
            .email(customer.getEmail())
            .taxId(customer.getTaxId())
            .phone(makePhone(customer.getPhone()))
            .birthDate(customer.getBirthDate())
            .address(makeAddress(customer.getAddress()))
            .billingInfo(makeBillingInfo(customer.getBillingInfo()))
            .build();
    }

    public static BillingInfo billingInfoFrom(com.recurrence.contract.domain.commons.BillingInfo billingInfo){
        return makeBillingInfo(billingInfo);
    }

    private static List<Phone> makePhone(List<com.recurrence.contract.domain.commons.Phone> phones){
        if (phones == null || phones.isEmpty()){
            return null;
        }
        // Todo validar se tem todos os campos necessarios
        return phones.stream().map(
            phone -> new Phone(
                phone.id(),
                phone.area(),
                phone.country(),
                phone.number()
            )
        ).toList();

    }

    private static Address makeAddress(com.recurrence.contract.domain.commons.Address address) {
        if (address == null) return null;

        // Todo validar se tem todos os campos necessarios
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

    private static BillingInfo makeBillingInfo(com.recurrence.contract.domain.commons.BillingInfo billingInfo){
        // Todo validar billingInfo, os validates deveriam ficar mesmo aqui?
        //  ou deveriam ficar dentro de uma classe validation que é chamada no service? (essa segunda abordagem me parece melhor com relação a divisão de responsabilidade)

        // Todo ajustar para pegar os dados sem perigo de dar null pointer
        return new BillingInfo(
            billingInfo.type(),
            new com.recurrence.pagbank.domain.commons.Card(
                billingInfo.card().token(),
                billingInfo.card().number(),
                billingInfo.card().encrypted(),
                billingInfo.card().brand(),
                null,
                null,
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
}
