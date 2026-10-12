package com.recurrence.exception;

/**
 * Um item de erro devolvido pelo provedor, já normalizado.
 *
 * @param code    código do erro no provedor (ex.: PagBank {@code error}, Mercado Pago {@code cause.code})
 * @param message descrição legível do erro
 * @param field   campo da requisição relacionado ao erro, quando o provedor informa
 */
public record ApiError(
        String code,
        String message,
        String field
) {
}
