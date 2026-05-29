package com.recurrence.pagbank.mapper;

import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.pagbank.domain.error.PagBankErrorList;

import java.util.List;

public final class PagBankErrorMapper {

    public static RequestResponse.RequestErrorResponseList to(PagBankErrorList errorList) {
        if (errorList == null || errorList.error_messages() == null || errorList.error_messages().isEmpty()) {
            return new RequestResponse.RequestErrorResponseList(
                    List.of(new RequestResponse.RequestErrorResponseDetail(
                            "pagbank_error",
                            "PagBank returned an error without details",
                            "PagBank returned an error without details"
                    ))
            );
        }

        return new RequestResponse.RequestErrorResponseList(
            errorList.error_messages()
                .stream()
                .map(error -> new RequestResponse.RequestErrorResponseDetail(
                    error.error(),
                    error.description(),
                    error.description()
                ))
                .toList()
        );
    }

}
