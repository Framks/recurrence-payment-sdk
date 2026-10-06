package com.recurrence.pagbank;

import com.recurrence.contract.InvoiceService;
import com.recurrence.contract.PlanService;
import com.recurrence.contract.RecurrenceProvider;
import com.recurrence.contract.SubscriptionService;
import com.recurrence.contract.config.Environment;
import com.recurrence.contract.config.Provider;
import com.recurrence.contract.config.ProviderConfig;
import com.recurrence.core.ProviderContext;
import com.recurrence.exception.RecurrenceConfigurationException;
import com.recurrence.infrastructure.HttpClientAdapter;
import com.recurrence.pagbank.mapper.PagBankErrorMapper;
import com.recurrence.pagbank.service.PagBankInvoiceService;
import com.recurrence.pagbank.service.PagBankPlanService;
import com.recurrence.pagbank.service.PagBankSubscriptionService;

import static com.recurrence.constants.ErrorMessage.PAGBANK_AUTHENTICATION_HEADERS_TOKEN;
import static com.recurrence.constants.ErrorMessage.PAGBANK_NOT_FOUND_TOKEN;
import static com.recurrence.constants.ErrorMessage.PAGBANK_NOT_SUPPORT_BACK_URL;

public class PagBankProvider implements RecurrenceProvider {

    private static final String SANDBOX_URL = "https://sandbox.api.assinaturas.pagseguro.com";
    private static final String PRODUCTION_URL = "https://api.assinaturas.pagseguro.com";

    private final PlanService planService;
    private final SubscriptionService subscriptionService;
    private final InvoiceService invoiceService;

    public PagBankProvider(
            Environment environment,
            ProviderConfig config
    ){
        this(environment, config, new HttpClientAdapter());
    }

    /** Permite injetar o cliente HTTP (ex.: em testes). */
    public PagBankProvider(
            Environment environment,
            ProviderConfig config,
            HttpClientAdapter httpClient
    ){
        validateConfig(config);
        ProviderContext context = new ProviderContext(
                Provider.PAGBANK,
                baseUrl(environment),
                config,
                httpClient,
                new PagBankErrorMapper()
        );

        this.planService = new PagBankPlanService(context);
        this.subscriptionService = new PagBankSubscriptionService(context);
        this.invoiceService = new PagBankInvoiceService(context);
    }

    @Override
    public PlanService planService() {
        return planService;
    }

    @Override
    public SubscriptionService subscriptionService() {
        return subscriptionService;
    }

    @Override
    public InvoiceService invoiceService() {
        return invoiceService;
    }

    private static String baseUrl(Environment environment) {
        return switch (environment) {
            case SANDBOX -> SANDBOX_URL;
            case PRODUCTION -> PRODUCTION_URL;
        };
    }

    private static void validateConfig(ProviderConfig config){
        if (config.getToken().isBlank()) {
            throw new RecurrenceConfigurationException(PAGBANK_NOT_FOUND_TOKEN);
        }
        boolean hasAuthorizationHeader = config.getDefaultHeaders()
                .keySet()
                .stream()
                .anyMatch("Authorization"::equalsIgnoreCase);
        if (hasAuthorizationHeader) {
            throw new RecurrenceConfigurationException(PAGBANK_AUTHENTICATION_HEADERS_TOKEN);
        }
        if (config.getBackUrl() != null && !config.getBackUrl().isBlank()) {
            throw new RecurrenceConfigurationException(PAGBANK_NOT_SUPPORT_BACK_URL);
        }
    }
}
