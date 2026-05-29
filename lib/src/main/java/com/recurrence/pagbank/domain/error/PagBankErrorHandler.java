package com.recurrence.pagbank.domain.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.core.Converter;
import com.recurrence.pagbank.mapper.PagBankErrorMapper;

import java.util.List;

public class PagBankErrorHandler {

    public static <T> RequestResponse<T> handle(
            RequestResponse<T> response,
            String body
    ) {
        if (body == null || body.isBlank()) {
            return response.setErrors(fallbackErrors("pagbank_error", "PagBank returned an error without response body"));
        }

        try {
            var errors = Converter.mapper()
                    .readValue(body, PagBankErrorList.class);

            return response.setErrors(PagBankErrorMapper.to(errors));
        } catch (JsonProcessingException e) {
            return response.setErrors(fallbackErrors("pagbank_error", body));
        }
    }

    private static RequestResponse.RequestErrorResponseList fallbackErrors(String key, String message) {
        return new RequestResponse.RequestErrorResponseList(
                List.of(new RequestResponse.RequestErrorResponseDetail(
                        key,
                        message,
                        message
                ))
        );
    }
}
