package com.recurrence.contract;

import com.recurrence.contract.config.Environment;
import com.recurrence.contract.config.Provider;
import com.recurrence.contract.config.ProviderConfig;
import com.recurrence.core.ProviderFactory;
import com.recurrence.exception.RecurrenceException;
import org.jetbrains.annotations.NotNull;

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
            @NotNull final Provider provider,
            @NotNull final Environment environment,
            @NotNull final ProviderConfig config
            ) throws RecurrenceException {
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
