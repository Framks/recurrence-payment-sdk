package com.recurrence.exception;

/**
 * Categoria de um erro devolvido pelo provedor, derivada do status HTTP.
 *
 * <p>Permite ao consumidor tratar o erro sem conhecer o provedor ativo nem o
 * formato de erro dele.</p>
 */
public enum ErrorCategory {
    /** 400 e 422: payload ou parâmetros recusados pelo provedor. */
    INVALID_REQUEST,
    /** 401 e 403: token ausente, inválido ou sem permissão. */
    AUTHENTICATION,
    /** 404: recurso inexistente. */
    NOT_FOUND,
    /** 409: conflito de estado ou chave de idempotência reutilizada. */
    CONFLICT,
    /** 429: limite de requisições atingido. */
    RATE_LIMITED,
    /** 5xx: falha interna do provedor. */
    PROVIDER_ERROR,
    /** Qualquer outro status fora de 2xx. */
    UNKNOWN;

    public static ErrorCategory fromStatus(int statusCode) {
        return switch (statusCode) {
            case 400, 422 -> INVALID_REQUEST;
            case 401, 403 -> AUTHENTICATION;
            case 404 -> NOT_FOUND;
            case 409 -> CONFLICT;
            case 429 -> RATE_LIMITED;
            default -> statusCode >= 500 && statusCode < 600 ? PROVIDER_ERROR : UNKNOWN;
        };
    }
}
