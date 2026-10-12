package com.recurrence.contract;

import com.recurrence.contract.config.Environment;
import com.recurrence.contract.config.Provider;
import com.recurrence.contract.config.ProviderConfig;
import com.recurrence.core.ProviderFactory;
import com.recurrence.exception.RecurrenceException;

import java.util.Objects;

final public class Recurrence {

    private final RecurrenceProvider provider;

    private Recurrence(final RecurrenceProvider provider) {
        this.provider = provider;
    }

    /**
     * Função responsavel por configurar um provedor.
     *
     * <p>Configura um provedor passando o enum provider, o enum do ambiente que estara executando e
     * as configurações basicas para esta integrando com ele. Retorno sera a classe Recurrence
     * com o provedor configurado</p>
     *
     * <pre>{@code
     * Recurrence config = Recurrence.config(
     *      Provider.PAGBANK,
     *      Enviroment.SANDBOX,
     *      ProviderConfig.builder()
     *          .token("your-token")
     *          .timeout(Duration.ofSeconds(15))  // Default 15s
     *          .defaultHeader("Accept", "application/json") // opcional
     *          .build()
     * );
     * }</pre>
     */
    public static Recurrence config(
            final Provider provider,
            final Environment environment,
            final ProviderConfig config
            ) throws RecurrenceException {
        Objects.requireNonNull(provider, "provider must not be null");
        Objects.requireNonNull(environment, "environment must not be null");
        Objects.requireNonNull(config, "config must not be null");
        return new Recurrence(
                ProviderFactory.create(provider, environment, config)
        );
    }

    public PlanService plan() {
        return provider.planService();
    }
    public SubscriptionService subscription() {
        return provider.subscriptionService();
    }
    public InvoiceService invoice() {
        return provider.invoiceService();
    }
}
