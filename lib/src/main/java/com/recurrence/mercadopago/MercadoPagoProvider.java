package com.recurrence.mercadopago;

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
import com.recurrence.mercadopago.mapper.MercadoPagoErrorMapper;
import com.recurrence.mercadopago.service.MercadoPagoInvoiceService;
import com.recurrence.mercadopago.service.MercadoPagoPlanService;
import com.recurrence.mercadopago.service.MercadoPagoSubscriptionService;

import static com.recurrence.constants.ErrorMessage.MERCADO_PAGO_AUTHENTICATION_HEADERS_TOKEN;
import static com.recurrence.constants.ErrorMessage.MERCADO_PAGO_BACK_URL_REQUIRED;
import static com.recurrence.constants.ErrorMessage.MERCADO_PAGO_NOT_FOUND_TOKEN;
import static com.recurrence.mercadopago.constant.Constant.BASE_URL;

/**
 * Provedor Mercado Pago.
 *
 * <p>Sandbox e produção usam a mesma URL; o ambiente é definido pela credencial
 * (credenciais de teste ou de usuário de teste). {@code backUrl} é obrigatório.</p>
 */
public class MercadoPagoProvider implements RecurrenceProvider {

    private final PlanService planService;
    private final SubscriptionService subscriptionService;
    private final InvoiceService invoiceService;

    public MercadoPagoProvider(
        Environment environment,
        ProviderConfig config
    ){
        this(environment, config, new HttpClientAdapter());
    }

    /** Permite injetar o cliente HTTP (ex.: em testes). */
    public MercadoPagoProvider(
            Environment environment,
            ProviderConfig config,
            HttpClientAdapter httpClient
    ){
        validateConfig(config);
        ProviderContext context = new ProviderContext(
                Provider.MERCADO_PAGO,
                BASE_URL,
                config,
                httpClient,
                new MercadoPagoErrorMapper()
        );

        this.planService = new MercadoPagoPlanService(context);
        this.subscriptionService = new MercadoPagoSubscriptionService(context);
        this.invoiceService = new MercadoPagoInvoiceService(context);
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

    private static void validateConfig(ProviderConfig config) {
        if (config.getToken().isBlank()) {
            throw new RecurrenceConfigurationException(MERCADO_PAGO_NOT_FOUND_TOKEN);
        }
        boolean hasAuthorizationHeader = config.getDefaultHeaders()
                .keySet()
                .stream()
                .anyMatch("Authorization"::equalsIgnoreCase);
        if (hasAuthorizationHeader) {
            throw new RecurrenceConfigurationException(MERCADO_PAGO_AUTHENTICATION_HEADERS_TOKEN);
        }
        if (config.getBackUrl() == null || config.getBackUrl().isBlank()) {
            throw new RecurrenceConfigurationException(MERCADO_PAGO_BACK_URL_REQUIRED);
        }
    }
}
