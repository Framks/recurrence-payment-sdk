package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.Coupon;
import com.recurrence.contract.domain.Customer;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.domain.commons.Address;
import com.recurrence.pagbank.domain.commons.PagBankAmount;
import com.recurrence.pagbank.domain.commons.Card;
import com.recurrence.pagbank.domain.commons.PagBankBillingInfo;
import com.recurrence.pagbank.domain.coupon.PagBankCoupon;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import com.recurrence.pagbank.domain.plan.PagBankPlan;
import com.recurrence.pagbank.domain.subscription.PagBankSubscription;
import com.recurrence.pagbank.domain.subscription.PagbankListSubscription;
import java.util.List;
import java.util.Objects;

public class PagBankSubscriptionMapper {

    public static PagBankSubscription toPagBankContract(Subscription subscription){
        return PagBankSubscription.builder()
            .referenceId(subscription.getReferenceId())
            .proRata(subscription.getProRata())
            .coupon(toPagBankCoupon(subscription))
            .amount(toPagBankAmount(subscription))
            .plan(toPagBankPlan(subscription))
            .paymentMethod(toPagBankBillingInfo(subscription.getPaymentMethod()))
            .customer(toPagBankCustomer(subscription))
            .bestInvoiceDate(new PagBankSubscription.BestInvoiceDate(
                subscription.getBestInvoiceDate().day(),
                subscription.getBestInvoiceDate().month()
            ))
            .splitEnabled(subscription.getSplitEnabled())
            .splits(toPagBankSplits(subscription))
            .build();
    }

    public static Subscription toContract(PagBankSubscription pagBankSubscription){
        return Subscription.builder()
            .id(pagBankSubscription.getId())
            .referenceId(pagBankSubscription.getReferenceId())
            .status(pagBankSubscription.getStatus())
            .proRata(pagBankSubscription.getProRata())
            .nextInvoiceAt(pagBankSubscription.getNextInvoiceAt())
            .coupon(toContractCoupon(pagBankSubscription))
            .amount(toContractAmount(pagBankSubscription))
            .plan(toContractPlan(pagBankSubscription))
            .paymentMethod(toContractBillingInfo(pagBankSubscription.getPaymentMethod()))
            .customer(toContractCustomer(pagBankSubscription))
            .expAt(pagBankSubscription.getExpAt())
            .createdAt(pagBankSubscription.getCreatedAt())
            .updatedAt(pagBankSubscription.getUpdatedAt())
            .splitEnabled(pagBankSubscription.getSplitEnabled())
            .splits(toContractSplits(pagBankSubscription))
            .build();
    }

    public static PageResponse<Subscription> toPageResponse(PagbankListSubscription listSubs){
        return new PageResponse<>(
            new PageResponse.PageInfo(
                listSubs.getResultSet().offset(),
                listSubs.getResultSet().limit(),
                listSubs.getResultSet().total()
            ),
            listSubs.getSubscriptions()
                .stream()
                .map(PagBankSubscriptionMapper::toContract)
                .toList()
        );
    }

    private static Subscription.Receiver toContractReceiver(PagBankSubscription.Receiver receiver){
            return new Subscription.Receiver(
                receiver.account().id(),
                receiver.amount().value()
            );
    }

    private static Plan toContractPlan(PagBankSubscription pagBankSubscription){
        return Plan.builder()
            .id(pagBankSubscription.getPlan().getId())
            .name(pagBankSubscription.getPlan().getName())
            .interval(
                pagBankSubscription.getPlan().getInterval().length(),
                pagBankSubscription.getPlan().getInterval().unit()
            )
            .build();
    }

    private static PagBankPlan toPagBankPlan(Subscription subscription){
        return PagBankPlan.builder()
            .id(subscription.getPlan().getId())
            .build();
    }

    private static Customer toContractCustomer(PagBankSubscription pagBankSubscription){
        Address address = null;

        if (pagBankSubscription.getCustomer().getAddress() != null) {
            address = new Address(
                pagBankSubscription.getCustomer().getAddress().street(),
                pagBankSubscription.getCustomer().getAddress().number(),
                pagBankSubscription.getCustomer().getAddress().complement(),
                pagBankSubscription.getCustomer().getAddress().locality(),
                pagBankSubscription.getCustomer().getAddress().city(),
                pagBankSubscription.getCustomer().getAddress().regionCode(),
                pagBankSubscription.getCustomer().getAddress().country(),
                pagBankSubscription.getCustomer().getAddress().postalCode()
            );
        }
        return Customer.builder()
            .id(pagBankSubscription.getCustomer().getId())
            .name(pagBankSubscription.getCustomer().getName())
            .email(pagBankSubscription.getCustomer().getEmail())
            .address(address)
            .build();
    }

    private static PagBankCustomer toPagBankCustomer(Subscription subscription){
        var customer = subscription.getCustomer();

        if (customer.getId() != null){
            return PagBankCustomer.builder()
                .id(customer.getId())
                .build();
        }

        List<PagBankBillingInfo> pagBankBillingInfo = null;
        if (subscription.getPaymentMethod().get(0).type() == "CREDIT_CARD"){
            pagBankBillingInfo = List.of(CommonsMapper.toPagBankBillingInfo(customer.getBillingInfo()));
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

    private static List<PagBankBillingInfo> toPagBankBillingInfo(List<BillingInfo> billingInfos){
        if (billingInfos == null || billingInfos.isEmpty()) {
            throw new IllegalArgumentException("Payment method must be of type 'bank_account'");
        }

        var billingInfo = billingInfos.get(0);

        if (billingInfo.type() == "BOLETO") {
            return List.of(
                new PagBankBillingInfo(
                    billingInfo.type(),
                    null
                )
            );
        }else {
            return List.of(
                new PagBankBillingInfo(
                    billingInfo.type(),
                    new Card(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        billingInfo.card().securityCode()
                    )
                )
            );
        }
    }

    private static List<BillingInfo> toContractBillingInfo(List<PagBankBillingInfo> pagBankBillingInfos){
        if (pagBankBillingInfos == null || pagBankBillingInfos.isEmpty()) {
            return null;
        }

        var pagBankBillingInfo = pagBankBillingInfos.get(0);

        if (Objects.equals(pagBankBillingInfo.type(), "BOLETO")) {
            return List.of(
                new BillingInfo(
                    pagBankBillingInfo.type(),
                    null
                )
            );
        }else {
            return List.of(
                new BillingInfo(
                    pagBankBillingInfo.type(),
                    new com.recurrence.contract.domain.commons.Card(
                        pagBankBillingInfo.card().number(),
                        pagBankBillingInfo.card().expYear(),
                        pagBankBillingInfo.card().expMonth(),
                        pagBankBillingInfo.card().encrypted(),
                        new com.recurrence.contract.domain.commons.Holder(
                            pagBankBillingInfo.card().holder().name()
                        ),
                        pagBankBillingInfo.card().token(),
                        pagBankBillingInfo.card().brand(),
                        pagBankBillingInfo.card().firstDigits(),
                        pagBankBillingInfo.card().lastDigits(),
                        null
                    )
                )
            );
        }
    }

    private static PagBankAmount toPagBankAmount(Subscription subscription){
        return new PagBankAmount(
            subscription.getAmount().value(),
            subscription.getAmount().currency()
        );
    }

    private static com.recurrence.contract.domain.commons.Amount toContractAmount(PagBankSubscription subscription){
        return new com.recurrence.contract.domain.commons.Amount(subscription.getAmount().value(), subscription.getAmount().currency());
    }

    private static PagBankCoupon toPagBankCoupon(Subscription subscription){
        return PagBankCoupon.builder()
            .id(subscription.getCoupon().getId())
            .build();
    }

    private static Coupon toContractCoupon(PagBankSubscription pagBankSubscription){
        if (pagBankSubscription.getCoupon() == null) return null;

        return Coupon.builder()
            .id(pagBankSubscription.getCoupon().getId())
            .name(pagBankSubscription.getCoupon().getName())
            .discount(pagBankSubscription.getCoupon().getDiscount().type(), pagBankSubscription.getCoupon().getDiscount().value())
            .build();
    }

    private static PagBankSubscription.Splits toPagBankSplits(Subscription subscription){
        PagBankSubscription.Splits splits = null;

        if (subscription.getSplitEnabled()){
            splits = new PagBankSubscription.Splits(
                subscription.getSplits().method(),
                subscription.getSplits().receivers().stream().map(
                    receiver -> new PagBankSubscription.Receiver(
                        new PagBankSubscription.Account(receiver.accountId()),
                        new PagBankAmount(receiver.amount(), null)
                    )
                ).toList()
            );
        }

        return splits;
    }

    private static Subscription.Splits toContractSplits(PagBankSubscription pagBankSubscription){
        Subscription.Splits splits = null;

        if (pagBankSubscription.getSplitEnabled()) {
            splits = new Subscription.Splits(
                pagBankSubscription.getSplits().method(),
                pagBankSubscription
                    .getSplits()
                    .receivers()
                    .stream()
                    .map(PagBankSubscriptionMapper::toContractReceiver)
                    .toList()
            );
        }

        return splits;
    }

    // ToDo ajustar para que o mapper não chame outros mappers pois isso vai acabar com a manutenção,
    //  o ideal é que cada mapper seja responsável apenas por um objeto,
    //  e os objetos relacionados sejam mapeados em seus respectivos mappers.
    //  quebrando o principio de responsabilidade única, o que pode levar a um código mais difícil de manter e testar.
}
