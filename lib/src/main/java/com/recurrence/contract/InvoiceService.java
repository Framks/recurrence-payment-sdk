package com.recurrence.contract;

import com.recurrence.contract.domain.Invoice;
import com.recurrence.contract.domain.Payment;
import com.recurrence.contract.filters.PaymentFilter;
import com.recurrence.contract.filters.PageOptions;
import com.recurrence.contract.infra.PageResponse;
import com.recurrence.exception.RecurrenceException;

/**
 * Consulta de faturas e pagamentos gerados pelas assinaturas. Obtido por
 * {@link Recurrence#invoice()}.
 *
 * <p>Fatura é a cobrança de um ciclo (PagBank {@code invoice}, Mercado Pago
 * {@code authorized_payment}); pagamento é uma tentativa de cobrança dessa
 * fatura (PagBank {@code payment}, Mercado Pago {@code /v1/payments}). Todas as
 * operações são só de leitura.</p>
 *
 * <p>Os métodos lançam subclasses de {@link RecurrenceException} (não checadas):
 * {@code RecurrenceValidationException} para pedido inválido,
 * {@code RecurrenceApiException} quando o provedor responde fora de 2xx e
 * {@code RecurrenceNetworkException} quando não há resposta.</p>
 */
public interface InvoiceService {

    /**
     * Consulta uma fatura pelo id.
     *
     * <p>No Mercado Pago a fatura não traz {@code occurrence} nem {@code plan}
     * (ficam {@code null}) e o assinante vem só com o id.</p>
     *
     * @param invoiceId id da fatura no provedor
     * @return a fatura
     * @throws com.recurrence.exception.RecurrenceValidationException se {@code invoiceId} estiver em branco
     * @throws com.recurrence.exception.RecurrenceApiException com categoria {@code NOT_FOUND} se a fatura não existir
     */
    Invoice getInvoice(String invoiceId) throws RecurrenceException;

    /**
     * Consulta um pagamento pelo id.
     *
     * <p>No Mercado Pago o pagamento não informa a fatura de origem, então
     * {@code invoiceId} fica {@code null}.</p>
     *
     * @param paymentId id do pagamento no provedor
     * @return o pagamento
     * @throws com.recurrence.exception.RecurrenceValidationException se {@code paymentId} estiver em branco
     * @throws com.recurrence.exception.RecurrenceApiException com categoria {@code NOT_FOUND} se o pagamento não existir
     */
    Payment getPayment(String paymentId) throws RecurrenceException;

    /**
     * Lista os pagamentos de uma fatura, sem filtro.
     *
     * @param invoiceId id da fatura no provedor
     * @return os pagamentos da fatura
     * @throws RecurrenceException se o pedido for inválido ou o provedor recusar
     * @see #listPayments(String, PaymentFilter)
     */
    default PageResponse<Payment> listPayments(String invoiceId) throws RecurrenceException {
        return listPayments(invoiceId, null);
    }

    /**
     * Pagamentos (tentativas de cobrança) de uma fatura.
     *
     * <p>No PagBank usa {@code GET /invoices/{id}/payments}. No Mercado Pago a
     * fatura referencia no máximo um pagamento: o adaptador busca a fatura, depois
     * o pagamento, e aplica o filtro sobre esse resultado (página com zero ou um item).</p>
     *
     * @param invoiceId     id da fatura no provedor
     * @param paymentFilter filtros; {@code null} não filtra
     * @return os pagamentos da fatura
     * @throws com.recurrence.exception.RecurrenceValidationException se {@code invoiceId} estiver em branco ou o
     *         filtro for inválido para o provedor ativo
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    PageResponse<Payment> listPayments(String invoiceId, PaymentFilter paymentFilter) throws RecurrenceException;

    /**
     * Lista pagamentos da conta, sem filtro.
     *
     * @param pageOptions paginação; {@code null} usa o padrão do provedor
     * @return uma página de pagamentos
     * @throws RecurrenceException se o provedor recusar a busca
     * @see #listPayments(PageOptions, PaymentFilter)
     */
    default PageResponse<Payment> listPayments(PageOptions pageOptions) throws RecurrenceException {
        return listPayments(pageOptions, null);
    }

    /**
     * Lista pagamentos da conta (PagBank {@code GET /payments}; Mercado Pago
     * {@code GET /v1/payments/search}, do mais recente para o mais antigo).
     *
     * <p>No Mercado Pago a busca devolve também pagamentos que não vieram de
     * assinaturas.</p>
     *
     * @param pageOptions   paginação; {@code null} usa o padrão do provedor
     * @param paymentFilter filtros; {@code null} não filtra
     * @return uma página de pagamentos
     * @throws com.recurrence.exception.RecurrenceValidationException se o filtro for inválido para o provedor
     *         ativo (datas sem par, intervalo acima de 90 dias no PagBank, status {@code UNPAID} no Mercado Pago)
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    PageResponse<Payment> listPayments(PageOptions pageOptions, PaymentFilter paymentFilter) throws RecurrenceException;

    /**
     * Lista as faturas de uma assinatura (PagBank {@code GET /subscriptions/{id}/invoices};
     * Mercado Pago {@code GET /authorized_payments/search?preapproval_id=...}).
     *
     * @param subscriptionId id da assinatura no provedor
     * @param pageOptions    paginação; {@code null} usa o padrão do provedor
     * @return uma página de faturas
     * @throws com.recurrence.exception.RecurrenceValidationException se {@code subscriptionId} estiver em branco
     * @throws com.recurrence.exception.RecurrenceApiException se o provedor responder fora de 2xx
     */
    PageResponse<Invoice> listInvoiceBySubscription(String subscriptionId, PageOptions pageOptions) throws RecurrenceException;
}
