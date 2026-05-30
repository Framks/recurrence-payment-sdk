package com.recurrence.contract.config;

import com.recurrence.contract.RecurrenceProvider;
import com.recurrence.pagbank.PagBankProvider;

public final class ProviderFactory {

    private ProviderFactory(){}

    public static RecurrenceProvider create(
            final Provider provider,
            final Environment environment,
            final ProviderConfig config
    ){
        return switch (provider){
            case PAGBANK -> new PagBankProvider(environment, config);
            case STRIPE -> null;
            case MERCADO_PAGO -> null;
        };
    }
}
