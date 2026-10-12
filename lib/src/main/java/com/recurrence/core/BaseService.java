package com.recurrence.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recurrence.contract.infra.RequestOptions;
import com.recurrence.exception.RecurrenceApiException;
import com.recurrence.exception.RecurrenceSerializationException;
import com.recurrence.infrastructure.HttpRequestRecurrence;
import com.recurrence.infrastructure.MethodRequest;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Pipeline comum de chamada aos provedores.
 *
 * <p>O service concreto só monta o caminho/payload e informa como mapear a
 * resposta do provedor para o contrato:</p>
 *
 * <pre>{@code
 * public Plan get(String planId) {
 *     return execute(
 *         "plan.get",
 *         request(MethodRequest.GET, "/plans/" + planId, RequestOptions.empty()).build(),
 *         PagBankPlan.class,
 *         PagBankPlanMapper::toContract
 *     );
 * }
 * }</pre>
 *
 * <p>Respostas 2xx são desserializadas e mapeadas; qualquer outro status vira
 * {@link RecurrenceApiException} com os erros traduzidos pelo
 * {@link ProviderErrorTranslator} do provedor.</p>
 */
public abstract class BaseService {

    private static final String IDEMPOTENCY_HEADER = "X-Idempotency-Key";

    protected final ProviderContext context;

    protected BaseService(ProviderContext context) {
        this.context = context;
    }

    /**
     * Inicia uma requisição já com URL, headers padrão, autenticação,
     * idempotência e timeout resolvidos.
     *
     * @param path caminho relativo à URL base, podendo conter query string
     */
    protected HttpRequestRecurrence.Builder request(MethodRequest method, String path, RequestOptions options) {
        RequestOptions resolved = options == null ? RequestOptions.empty() : options;

        Map<String, String> headers = new HashMap<>(context.config().getDefaultHeaders());
        headers.put("Authorization", "Bearer " + context.config().getToken());
        if (resolved.hasIdempotencyKey()) {
            headers.put(IDEMPOTENCY_HEADER, resolved.getIdempotencyKey());
        }

        return HttpRequestRecurrence.builder()
                .method(method)
                .url(context.baseUrl() + path)
                .headers(headers)
                .timeout(resolved.getTimeout() != null ? resolved.getTimeout() : context.config().getTimeout());
    }

    /**
     * Executa a requisição e mapeia o corpo 2xx para o tipo do contrato.
     *
     * @param operation    nome da operação, usado nas mensagens de erro (ex.: {@code "plan.create"})
     * @param responseType DTO do provedor para desserializar o corpo
     * @param toContract   conversão do DTO do provedor para o contrato
     */
    protected <P, T> T execute(
            String operation,
            HttpRequestRecurrence request,
            Class<P> responseType,
            Function<P, T> toContract
    ) {
        HttpResponse<String> response = send(operation, request);
        return toContract.apply(fromJson(operation, response.body(), responseType));
    }

    /** Executa a requisição descartando o corpo (ativar, cancelar, etc.). */
    protected void execute(String operation, HttpRequestRecurrence request) {
        send(operation, request);
    }

    /** Codifica um valor (ex.: id) para uso seguro como segmento do caminho. */
    protected static String segment(String value) { // ToDo Validar se isso é nescessário
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    protected String toJson(String operation, Object payload) {
        try {
            return Converter.mapper().writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RecurrenceSerializationException(
                    context.provider() + " " + operation + ": failed to serialize request", e);
        }
    }

    protected <P> P fromJson(String operation, String body, Class<P> type) {
        try {
            return Converter.mapper().readValue(body, type);
        } catch (JsonProcessingException e) {
            throw new RecurrenceSerializationException(
                    context.provider() + " " + operation + ": failed to deserialize response", e);
        }
    }

    private HttpResponse<String> send(String operation, HttpRequestRecurrence request) {
        HttpResponse<String> response = context.httpClient().send(request);
        int status = response.statusCode();
        if (status / 100 != 2) {
            throw new RecurrenceApiException(
                    status,
                    context.provider(),
                    operation,
                    context.errorTranslator().translate(status, response.body()),
                    response.body()
            );
        }
        return response;
    }
}
