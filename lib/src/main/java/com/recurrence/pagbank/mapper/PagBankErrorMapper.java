package com.recurrence.pagbank.mapper;

import com.recurrence.contract.infra.RequestResponse;
import com.recurrence.pagbank.domain.error.PagBankErrorList;

public final class PagBankErrorMapper {

    public static RequestResponse.RequestErrorResponseList to(PagBankErrorList errorList) {

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
