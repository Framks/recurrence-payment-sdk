package com.recurrence.pagbank.domain.error;

import com.recurrence.contract.domain.infra.RequestResponse;

public final class PagBankErrorMapper {

    public static RequestResponse.RequestErrorResponseList to(PagBankErrorList errorList) {

        return new RequestResponse.RequestErrorResponseList(
                errorList.errors()
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
