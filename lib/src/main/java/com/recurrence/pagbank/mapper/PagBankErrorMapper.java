package com.recurrence.pagbank.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recurrence.core.Converter;
import com.recurrence.core.ProviderErrorTranslator;
import com.recurrence.exception.ApiError;
import com.recurrence.pagbank.domain.error.PagBankErrorList;

import java.util.List;

/**
 * Traduz o corpo de erro do PagBank ({@code {"error_messages": [...]}}) para {@link ApiError}.
 */
public final class PagBankErrorMapper implements ProviderErrorTranslator {

    @Override
    public List<ApiError> translate(int statusCode, String body) {
        PagBankErrorList errorList = parse(body);
        if (errorList == null || errorList.error_messages() == null || errorList.error_messages().isEmpty()) {
            return List.of(new ApiError(
                    "pagbank_error",
                    "PagBank returned HTTP " + statusCode + " without details",
                    null
            ));
        }

        return errorList.error_messages()
                .stream()
                .map(error -> new ApiError(error.error(), error.description(), error.parameter_name()))
                .toList();
    }

    private static PagBankErrorList parse(String body) {
        if (body == null || body.isBlank()) {
            return null;
        }
        try {
            return Converter.mapper().readValue(body, PagBankErrorList.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
