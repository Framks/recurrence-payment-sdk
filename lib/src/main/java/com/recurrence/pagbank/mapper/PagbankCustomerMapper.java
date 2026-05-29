package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.Customer;
import com.recurrence.pagbank.domain.commons.PagBankBillingInfo;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.pagbank.domain.commons.Phone;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import com.recurrence.pagbank.domain.customer.PagBankListCustomer;
import java.util.List;
import java.util.Optional;

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
        var billingInfoList = customer.getBillingInfo();

        var billingInfoOpt = Optional.ofNullable(billingInfoList).filter(l -> !l.isEmpty()).map(l -> l.get(0)).orElse(null);

        return Customer.builder()
            .id(customer.getId())
            .referenceId(customer.getReferenceId())
            .name(customer.getName())
            .email(customer.getEmail())
            .taxId(customer.getTaxId())
            .createdAt(customer.getCreatedAt())
            .updatedAt(customer.getUpdatedAt())
            .phone(customer.getPhones().stream().map(CommonsMapper::toContractPhone).toList())
            .address(CommonsMapper.toContractAddress(customer.getAddress()))
            .billingInfo(CommonsMapper.toContractBillingInfo(billingInfoOpt))
            .birthDate(customer.getBirthDate())
                    
            .build();
    }

    public static PagBankCustomer toUpdate(Customer customer){
        List<PagBankBillingInfo> pagBankBillingInfo = null;
        List<Phone> phones = null;

        if (customer.getBillingInfo() != null){
            pagBankBillingInfo = CommonsMapper.toPagBankBillingInfo(customer.getBillingInfo());
        }

        if (customer.getPhone() != null ){
            phones = customer.getPhone().stream().map(CommonsMapper::toPagBankPhone).toList();
        }

        return PagBankCustomer.builder()
            .referenceId(customer.getReferenceId())
            .name(customer.getName())
            .email(customer.getEmail())
            .phone(phones)
            .birthDate(customer.getBirthDate())
            .address(CommonsMapper.toPagBankAddress(customer.getAddress()))
            .billingInfo(pagBankBillingInfo)
            .buildUpdate();
    }

    public static PagBankCustomer from(Customer customer){
        List<PagBankBillingInfo> pagBankBillingInfo = null;

        if (customer.getBillingInfo() != null){
            pagBankBillingInfo = CommonsMapper.toPagBankBillingInfo(customer.getBillingInfo());
        }

        return PagBankCustomer.builder()
            .referenceId(customer.getReferenceId())
            .name(customer.getName())
            .email(customer.getEmail())
            .taxId(customer.getTaxId())
            .phone(customer.getPhone().stream().map(CommonsMapper::toPagBankPhone).toList())
            .birthDate(customer.getBirthDate())
            .address(CommonsMapper.toPagBankAddress(customer.getAddress()))
            .billingInfo(pagBankBillingInfo)
            .build();
    }

}
