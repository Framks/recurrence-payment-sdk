package com.recurrence.pagbank.service;

import com.recurrence.contract.InvoiceService;
import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Payment;
import com.recurrence.contract.domain.Refund;
import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.core.BaseService;
import com.recurrence.pagbank.PagBankEnvironment;
import com.recurrence.pagbank.client.PagBankInvoiceClient;
import com.recurrence.pagbank.domain.error.PagBankErrorHandler;
import com.recurrence.pagbank.domain.payment.PagBankInvoice;
import com.recurrence.pagbank.domain.payment.PagBankListPayment;
import com.recurrence.pagbank.domain.payment.PagBankListRefund;
import com.recurrence.pagbank.domain.payment.PagBankPayment;
import com.recurrence.pagbank.domain.payment.PagBankRefund;
import com.recurrence.pagbank.mapper.PagBankInvoiceMapper;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PagBankInvoiceService extends BaseService implements InvoiceService {

    private final String token;
    private final PagBankInvoiceClient pagBankInvoiceClient;

    public PagBankInvoiceService(
        final PagBankEnvironment environment,
        final String token
    ){
        this.token = Objects.requireNonNull(token, "Token must be a valid value");
        this.pagBankInvoiceClient = new PagBankInvoiceClient(Objects.requireNonNull(environment, "Environment must be a valid value"));
    }

    @Override
    public RequestResponse<Invoice> getInvoice(String id) {
        try {
            var headers = defaultHeaders(token);
            HttpResponse<String> response = pagBankInvoiceClient.getInvoice(id, headers);
            RequestResponse<Invoice> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse.setBody(
                        PagBankInvoiceMapper.toContract(
                                fromJson(response.body(), PagBankInvoice.class)
                        )
                );
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Payment> getPayment(String id) {
        try {
            var headers = defaultHeaders(token);
            HttpResponse<String> response = pagBankInvoiceClient.getPayment(id, headers);
            RequestResponse<Payment> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse.setBody(
                        PagBankInvoiceMapper.toContract(
                                fromJson(response.body(), PagBankPayment.class)
                        )
                );
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Payment>> listPayments(Long offset, Long limit){
        return listPayment(null, offset, limit);
    }

    @Override
    public RequestResponse<PageResponse<Payment>> listPayments(String id) {
            return listPayment(id, null, null);
    }

    @Override
    public RequestResponse<Refund> refund(String paymentId, Amount amount) {
        try {
            var headers = defaultHeaders(token);
            String payload = toJson(PagBankInvoiceMapper.toRefundPayload(amount));
            HttpResponse<String> response = pagBankInvoiceClient.createRefund(paymentId, payload, headers);
            RequestResponse<Refund> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 200, 201 -> requestResponse.setBody(
                        PagBankInvoiceMapper.toContract(
                                fromJson(response.body(), PagBankRefund.class)
                        )
                );
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<Refund> getRefund(String id) {
        try {
            var headers = defaultHeaders(token);
            HttpResponse<String> response = pagBankInvoiceClient.getRefund(id, headers);
            RequestResponse<Refund> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse.setBody(
                        PagBankInvoiceMapper.toContract(
                                fromJson(response.body(), PagBankRefund.class)
                        )
                );
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Refund>> listRefunds(Long offset, Long limit) {
        try {
            var headers = defaultHeaders(token);
            HttpResponse<String> response = pagBankInvoiceClient.getRefunds(offset.intValue(), limit.intValue(), headers);
            RequestResponse<PageResponse<Refund>> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse.setBody(
                        PagBankInvoiceMapper.toPageResponse(
                                fromJson(response.body(), PagBankListRefund.class)
                        )
                );
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private RequestResponse<PageResponse<Payment>> listPayment(String id, Long offset, Long limit){
        try {
            var headers = defaultHeaders(token);
            HttpResponse<String> response = null;

            if (id == null || id.isEmpty()) {
                response = pagBankInvoiceClient.listPayments(offset.intValue(), limit.intValue(), headers);
            }else{
                response = pagBankInvoiceClient.listInvoicePayments(id, headers);
            }

            RequestResponse<PageResponse<Payment>> requestResponse = buildResponse(response);

            return switch (requestResponse.getHttpResponse()) {
                case 200 -> requestResponse.setBody(
                    PagBankInvoiceMapper.toPageResponse(
                        fromJson(response.body(), PagBankListPayment.class)
                    )
                );
                case 400 -> PagBankErrorHandler.handle(requestResponse, response.body());
                default -> requestResponse;
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
