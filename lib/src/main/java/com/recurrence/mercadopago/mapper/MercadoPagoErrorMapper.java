package com.recurrence.mercadopago.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recurrence.core.Converter;
import com.recurrence.core.ProviderErrorTranslator;
import com.recurrence.exception.ApiError;
import com.recurrence.mercadopago.domain.error.MercadoPagoError;

import java.util.List;

/**
 * Traduz o corpo de erro do Mercado Pago para {@link ApiError}: usa {@code cause}
 * quando presente; senão, {@code error}/{@code message}.
 */
public final class MercadoPagoErrorMapper implements ProviderErrorTranslator {

    @Override
    public List<ApiError> translate(int statusCode, String body) {
        MercadoPagoError error = parse(body);
        if (error == null) {
            return List.of(new ApiError(
                    "mercado_pago_error",
                    "Mercado Pago returned HTTP " + statusCode + " without details",
                    null
            ));
        }

        if (error.cause() != null && !error.cause().isEmpty()) {
            return error.cause()
                    .stream()
                    .map(cause -> new ApiError(cause.code(), cause.description(), null))
                    .toList();
        }

        return List.of(new ApiError(firstNonNull(error.code(), error.error(), String.valueOf(statusCode)), error.message(), null));
    }

    private static String firstNonNull(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) return value;
        }
        return null;
    }

    private static MercadoPagoError parse(String body) {
        if (body == null || body.isBlank()) {
            return null;
        }
        try {
            return Converter.mapper().readValue(body, MercadoPagoError.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
