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

/**
 * Ponto de entrada da biblioteca: configura o provedor ativo e dá acesso aos
 * services do contrato.
 *
 * <p>O código do integrador depende só desta classe e dos tipos de
 * {@code com.recurrence.contract}. Trocar de provedor é trocar a configuração
 * (por exemplo, a variável {@code RECURRENCE_PROVIDER}); as chamadas continuam as mesmas.</p>
 *
 * <pre>{@code
 * Recurrence recurrence = Recurrence.config(ProviderConfig.fromEnv());
 * Plan plan = recurrence.plan().get("PLAN_123");
 * }</pre>
 *
 * <p>A instância é imutável e os services são criados uma vez, na configuração.</p>
 */
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
     *
     * @param config configuração com o provedor definido (no builder ou em {@code RECURRENCE_PROVIDER})
     * @return a instância configurada para o provedor de {@code config}
     * @throws NullPointerException se {@code config} for {@code null}
     * @throws RecurrenceConfigurationException se o provedor não estiver definido ou a configuração não
     *         servir para ele (ex.: {@code backUrl} ausente no Mercado Pago ou presente no PagBank)
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
     *
     * @param provider    provedor a usar
     * @param environment ambiente do provedor ({@code SANDBOX} ou {@code PRODUCTION})
     * @param config      demais campos (token, timeout, headers, chave pública, {@code backUrl})
     * @return a instância configurada para {@code provider}
     * @throws NullPointerException se algum argumento for {@code null}
     * @throws RecurrenceConfigurationException se {@code config} já tiver outro provedor ou a configuração
     *         não servir para {@code provider}
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

    /**
     * Operações de plano.
     *
     * @return o {@link PlanService} do provedor ativo
     */
    public PlanService plan() {
        return provider.planService();
    }
    /**
     * Operações de assinatura.
     *
     * @return o {@link SubscriptionService} do provedor ativo
     */
    public SubscriptionService subscription() {
        return provider.subscriptionService();
    }
    /**
     * Consulta de faturas e pagamentos.
     *
     * @return o {@link InvoiceService} do provedor ativo
     */
    public InvoiceService invoice() {
        return provider.invoiceService();
    }
    /**
     * Tokenização de cartão no backend.
     *
     * @return o {@link CardService} do provedor ativo
     */
    public CardService card() {
        return provider.cardService();
    }
}
