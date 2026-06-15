package com.recurrence.pagbank.mapper;

import com.recurrence.contract.domain.Coupon;
import com.recurrence.contract.domain.Customer;
import com.recurrence.contract.domain.Plan;
import com.recurrence.contract.domain.Subscription;
import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.domain.commons.BillingInfo;
import com.recurrence.contract.domain.commons.Boleto;
import com.recurrence.contract.domain.commons.Card;
import com.recurrence.contract.domain.commons.Holder;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.domain.commons.Address;
import com.recurrence.pagbank.domain.commons.PagBankAmount;
import com.recurrence.pagbank.domain.commons.PagBankCard;
import com.recurrence.pagbank.domain.commons.PagBankBillingInfo;
import com.recurrence.pagbank.domain.commons.PagBankLink;
import com.recurrence.pagbank.domain.coupon.PagBankCoupon;
import com.recurrence.pagbank.domain.customer.PagBankCustomer;
import com.recurrence.pagbank.domain.plan.PagBankPlan;
import com.recurrence.pagbank.domain.subscription.PagBankSubscription;
import com.recurrence.pagbank.domain.subscription.PagbankListSubscription;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.recurrence.constants.MapperMessage.AMOUNT_CURRENCY;
import static com.recurrence.constants.MapperMessage.AMOUNT_NULL;
import static com.recurrence.constants.MapperMessage.AMOUNT_ZERO;
import static com.recurrence.constants.MapperMessage.BEST_INVOICE_DATE_NULL;
import static com.recurrence.constants.MapperMessage.BILLINGINFOS_NULL;
import static com.recurrence.constants.MapperMessage.CARD_NULL;
import static com.recurrence.constants.MapperMessage.CUSTOMER_NULL;
import static com.recurrence.constants.MapperMessage.PLAN_NULL;
import static com.recurrence.constants.PaymentMethod.BOLETO;
import static com.recurrence.constants.PaymentMethod.CREDIT_CARD;
import static com.recurrence.pagbank.mapper.CommonsMapper.toContractPaymentMethodType;
import static com.recurrence.pagbank.mapper.CommonsMapper.toPagBankPaymentMethodType;

public class PagBankSubscriptionMapper {

    public static PagBankSubscription toPagBankContract(Subscription subscription){
        //if ( subscription.getBestInvoiceDate() == null ) throw new IllegalArgumentException(BEST_INVOICE_DATE_NULL);

        return PagBankSubscription.builder()
            .referenceId(subscription.getReferenceId())
            .proRata(subscription.getProRata())
            .coupon(toPagBankCoupon(subscription))
            .amount(toPagBankAmount(subscription))
            .plan(toPagBankPlan(subscription))
            .paymentMethod(toPagBankBillingInfo(subscription.getPaymentMethod()))
            .customer(toPagBankCustomer(subscription))
            .bestInvoiceDate(toPagBankBestInvoiceDate(subscription.getBestInvoiceDate()))
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
            .paymentMethod(toContractBillingInfo(pagBankSubscription.getPaymentMethod(), pagBankSubscription.getLinks()))
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
        if ( subscription.getPlan() == null) throw new IllegalArgumentException(PLAN_NULL);

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
        if ( subscription.getCustomer() == null ) throw new IllegalArgumentException(CUSTOMER_NULL);

        var customer = subscription.getCustomer();

        if (customer.getId() != null){
            return PagBankCustomer.builder()
                .id(customer.getId())
                .build();
        }

        List<PagBankBillingInfo> pagBankBillingInfo = null;
        if (subscription.getPaymentMethod().get(0).type().name().equals(CREDIT_CARD)){
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

    private static List<PagBankBillingInfo> toPagBankBillingInfo(List<BillingInfo> billingInfos){
        if (billingInfos == null || billingInfos.isEmpty()) {
            throw new IllegalArgumentException(BILLINGINFOS_NULL);
        }

        var billingInfo = billingInfos.get(0);

        if (billingInfo.type().name().equals(BOLETO)) {
            return List.of(
                new PagBankBillingInfo(
                        toPagBankPaymentMethodType(billingInfo.type()),
                    null,
                    null
                )
            );
        } else {
            if ( billingInfo.card() == null) throw new IllegalArgumentException(CARD_NULL);
            // fazer uma validação que represente as regras de envio do cartão para o pagbank
            PagBankCard.Holder holder = null;
            if (billingInfo.card().holder() != null){
                holder = new PagBankCard.Holder(
                        billingInfo.card().holder().name(),
                        null,
                        null
                );
            }

            return List.of(
                new PagBankBillingInfo(
                        toPagBankPaymentMethodType(billingInfo.type()),
                    new PagBankCard(
                        billingInfo.card().token(),
                        billingInfo.card().number(),
                        billingInfo.card().encrypted(),
                        billingInfo.card().brand(),
                        null,
                        null,
                        billingInfo.card().expMonth(),
                        billingInfo.card().expYear(),
                        holder,
                        billingInfo.card().securityCode()
                    ),
                    null
                )
            );
        }
    }

    private static List<BillingInfo> toContractBillingInfo(
        List<PagBankBillingInfo> billingInfos,
        List<PagBankLink> links
    ) {
        if (billingInfos == null || billingInfos.isEmpty()) {
            return null;
        }

        var info = billingInfos.get(0);

        var linksByType = links.stream()
            .collect(Collectors.toMap(
                PagBankLink::media,
                PagBankLink::href,
                (a, b) -> a
            ));

        return List.of(
            new BillingInfo(
                    toContractPaymentMethodType(info.type()),
                mapCard(info),
                mapBoleto(info, linksByType)
            )
        );
    }

    private static Boleto mapBoleto(PagBankBillingInfo info, Map<String, String> links) {
        if (!BOLETO.equals(info.type()) || info.boleto() == null) {
            return null;
        }
        return new Boleto(
            info.boleto().id(),
            info.boleto().barcode(),
            info.boleto().formattedBarcode(),
            info.boleto().dueAt(),
            links.get("application/pdf"),
            links.get("image/png")
        );
    }

    private static Card mapCard(PagBankBillingInfo info) {
        if (!CREDIT_CARD.equals(info.type()) || info.card() == null) {
            return null;
        }

        return new Card(
            info.card().number(),
            info.card().expYear(),
            info.card().expMonth(),
            info.card().encrypted(),
            new Holder(info.card().holder().name()),
            info.card().token(),
            info.card().brand(),
            info.card().firstDigits(),
            info.card().lastDigits(),
            null
        );
    }

    private static PagBankAmount toPagBankAmount(Subscription subscription){
        if ( subscription.getAmount() == null) return null;
        if ( subscription.getAmount().value() <= 0) throw new IllegalArgumentException(AMOUNT_ZERO);
        if ( subscription.getAmount().currency() == null || subscription.getAmount().currency().isBlank()) throw new IllegalArgumentException(AMOUNT_CURRENCY);

        return new PagBankAmount(
            subscription.getAmount().value(),
            subscription.getAmount().currency()
        );
    }

    private static Amount toContractAmount(PagBankSubscription subscription){
        return new Amount(subscription.getAmount().value(), subscription.getAmount().currency());
    }

    private static PagBankCoupon toPagBankCoupon(Subscription subscription){
        if ( subscription.getCoupon() == null ) return null;

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

        if (subscription.getSplitEnabled() != null && subscription.getSplitEnabled() == true){
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

    private static PagBankSubscription.BestInvoiceDate toPagBankBestInvoiceDate(Subscription.BestInvoiceDate bestInvoiceDate){
        if (bestInvoiceDate == null ) return null;
        return new PagBankSubscription.BestInvoiceDate(
                bestInvoiceDate.day(),
                bestInvoiceDate.month()
        );
    }
}
