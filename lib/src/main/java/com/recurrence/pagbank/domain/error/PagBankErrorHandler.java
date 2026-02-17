package com.recurrence.pagbank.domain.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recurrence.contract.domain.infra.RequestResponse;
import com.recurrence.core.Converter;

public class PagBankErrorHandler {

    public static RequestResponse<?> handle(
            RequestResponse<?> response,
            String body
    ) throws JsonProcessingException {

        var errors = Converter.mapper()
                .readValue(body, PagBankErrorList.class);

        return response.setErrors(PagBankErrorMapper.to(errors));
    }
}
