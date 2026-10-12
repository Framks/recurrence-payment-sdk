package com.recurrence.contract;

import com.recurrence.contract.config.Environment;
import com.recurrence.contract.config.Provider;
import com.recurrence.contract.config.ProviderConfig;
import com.recurrence.core.EnvironmentConfigLoader;
import com.recurrence.core.ProviderFactory;
import com.recurrence.exception.RecurrenceConfigurationException;
import com.recurrence.exception.RecurrenceException;

import java.util.Objects;

import static com.recurrence.constants.ErrorMessage.CONFIG_PROVIDER_MISMATCH;
import static com.recurrence.constants.ErrorMessage.CONFIG_PROVIDER_REQUIRED;

final public class Recurrence {

    private final RecurrenceProvider provider;

    private Recurrence(final RecurrenceProvider provider) {
        this.provider = provider;
    }

    /**
     * Configura o provedor a partir do {@link ProviderConfig}, que já traz o
     * provedor e o ambiente (declarados no código ou lidos das variáveis de ambiente).
     *
     * <pre>{@code
     * // RECURRENCE_PROVIDER=PAGBANK, RECURRENCE_PAGBANK_TOKEN=..., RECURRENCE_PAGBANK_ENVIRONMENT=SANDBOX
     * Recurrence recurrence = Recurrence.config(ProviderConfig.fromEnv());
     * }</pre>
     */
    public static Recurrence config(final ProviderConfig config) throws RecurrenceException {
        Objects.requireNonNull(config, "config must not be null");
        if (config.getProvider() == null) {
            throw new RecurrenceConfigurationException(
                    String.format(CONFIG_PROVIDER_REQUIRED, EnvironmentConfigLoader.PROVIDER));
        }
        return new Recurrence(
                ProviderFactory.create(config.getProvider(), config.getEnvironment(), config)
        );
    }

    /**
     * Configura um provedor informando provedor e ambiente no código.
     *
     * <p>Provedor e ambiente passados aqui ganham dos que estiverem no
     * {@code config}. O {@code config} pode vir das variáveis de ambiente.</p>
     *
     * <pre>{@code
     * Recurrence config = Recurrence.config(
     *      Provider.PAGBANK,
     *      Environment.SANDBOX,
     *      ProviderConfig.builder()
     *          .token("your-token")
     *          .timeout(Duration.ofSeconds(15))  // Default 15s
     *          .defaultHeader("X-Custom", "value") // opcional
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
        if (config.getProvider() != null && config.getProvider() != provider) {
            throw new RecurrenceConfigurationException(
                    String.format(CONFIG_PROVIDER_MISMATCH, provider, config.getProvider()));
        }
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
    public CardService card() {
        return provider.cardService();
    }
}
