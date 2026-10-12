package com.recurrence.core;

import com.recurrence.exception.ApiError;

import java.util.List;

/**
 * Converte o corpo de erro de um provedor na lista normalizada de {@link ApiError}.
 *
 * <p>Cada provedor implementa apenas o parsing do seu formato. Status, categoria
 * e lançamento da exceção ficam no {@link BaseService}.</p>
 */
@FunctionalInterface
public interface ProviderErrorTranslator {

    /**
     * @param statusCode status HTTP da resposta
     * @param body       corpo da resposta, pode ser vazio ou não ser JSON
     * @return erros normalizados; nunca {@code null}
     */
    List<ApiError> translate(int statusCode, String body);
}
