package com.recurrence.pagbank.service;

import com.recurrence.contract.InvoiceService;
import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Payment;
import com.recurrence.contract.domain.Refund;
import com.recurrence.contract.domain.commons.Amount;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.core.BaseService;
import com.recurrence.pagbank.PagbankConfig;
import com.recurrence.pagbank.client.PagBankInvoiceClient;
import com.recurrence.pagbank.domain.error.PagBankErrorHandler;
import com.recurrence.pagbank.domain.payment.PagBankInvoice;
import com.recurrence.pagbank.domain.payment.PagBankListPayment;
import com.recurrence.pagbank.domain.payment.PagBankListRefund;
import com.recurrence.pagbank.domain.payment.PagBankPayment;
import com.recurrence.pagbank.domain.payment.PagBankRefund;
import com.recurrence.pagbank.mapper.PagBankInvoiceMapper;
import java.net.http.HttpResponse;

import static com.recurrence.constants.ProviderConstants.OPERATION_EXCEPTION;
import static com.recurrence.constants.ProviderConstants.PAGBANK;

public class PagBankInvoiceService extends BaseService implements InvoiceService {

    private final PagbankConfig config;
    private final PagBankInvoiceClient pagBankInvoiceClient;

    public PagBankInvoiceService(
        final PagbankConfig config
    ){
        this.config = config;
        this.pagBankInvoiceClient = new PagBankInvoiceClient(config);
    }

    @Override
    public RequestResponse<Invoice> getInvoice(String id) {
        try {
            var headers = config.fullHeaders();
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
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Payment> getPayment(String id) {
        try {
            var headers = config.fullHeaders();
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
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Payment>> listPayments(Integer offset, Integer limit){
        return listPayment(null, offset, limit);
    }

    @Override
    public RequestResponse<PageResponse<Payment>> listPayments(String id) {
            return listPayment(id, null, null);
    }

    @Override
    public RequestResponse<Refund> refund(String paymentId, Amount amount) {
        try {
            var headers = config.fullHeaders();
            String payload = toJson(PagBankInvoiceMapper.toRefundPayload(amount)); // toDo: aqui estou usando o objeto PagBankInvoice para enviar penas o amount que precisa, devemos fazer um dto para isso?
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
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<Refund> getRefund(String id) {
        try {
            var headers = config.fullHeaders();
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
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }

    @Override
    public RequestResponse<PageResponse<Refund>> listRefunds(Integer offset, Integer limit) {
        try {
            var headers = config.fullHeaders();
            HttpResponse<String> response = pagBankInvoiceClient.getRefunds(pageValue(offset, "offset"), pageValue(limit, "limit"), headers);
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
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }


    private RequestResponse<PageResponse<Payment>> listPayment(String id, Integer offset, Integer limit){
        try {
            var headers = config.fullHeaders();
            HttpResponse<String> response = null;

            if (id == null || id.isEmpty()) {
                response = pagBankInvoiceClient.listPayments(pageValue(offset, "offset"), pageValue(limit, "limit"), headers);
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
            throw providerException(PAGBANK, OPERATION_EXCEPTION, e);
        }
    }
}
