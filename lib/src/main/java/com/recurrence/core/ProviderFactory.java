package com.recurrence.core;

import com.recurrence.contract.RecurrenceProvider;
import com.recurrence.contract.config.Environment;
import com.recurrence.contract.config.Provider;
import com.recurrence.contract.config.ProviderConfig;
import com.recurrence.mercadopago.MercadoPagoProvider;
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
            case MERCADO_PAGO -> new MercadoPagoProvider(environment, config);
        };
    }
}
