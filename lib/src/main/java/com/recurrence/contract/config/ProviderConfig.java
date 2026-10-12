package com.recurrence.contract.config;

import com.recurrence.core.EnvironmentConfigLoader;
import com.recurrence.exception.RecurrenceConfigurationException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.recurrence.constants.ErrorMessage.CONFIG_TOKEN_REQUIRED;
import static com.recurrence.constants.ErrorMessage.CONFIG_TOKEN_REQUIRED_NO_PROVIDER;
import static com.recurrence.core.EnvironmentConfigLoader.BACK_URL;
import static com.recurrence.core.EnvironmentConfigLoader.PUBLIC_KEY;
import static com.recurrence.core.EnvironmentConfigLoader.TOKEN;

/**
 * Configuração usada para inicializar um provedor.
 *
 * <p>Pode ser declarada no código, lida das variáveis de ambiente ou as duas
 * coisas juntas. Quando um campo vem dos dois lados, <b>o valor declarado no
 * código ganha</b>. Nomes das variáveis em {@link EnvironmentConfigLoader}.</p>
 *
 * <pre>{@code
 * // Só código
 * ProviderConfig config = ProviderConfig.builder()
 *     .provider(Provider.PAGBANK)
 *     .environment(Environment.SANDBOX)
 *     .token("your-token")
 *     .timeout(Duration.ofSeconds(15))
 *     .build();
 *
 * // Só variáveis de ambiente (RECURRENCE_PROVIDER, RECURRENCE_PAGBANK_TOKEN, ...)
 * ProviderConfig config = ProviderConfig.fromEnv();
 *
 * // Variáveis de ambiente com o timeout sobrescrito no código
 * ProviderConfig config = ProviderConfig.builder()
 *     .fromEnv()
 *     .timeout(Duration.ofSeconds(30))
 *     .build();
 * }</pre>
 */
public class ProviderConfig {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);

    private final Provider provider;
    private final Environment environment;
    private final Duration timeout;
    private final String token;
    private final String publicKey;
    private final Map<String, String> defaultHeaders;
    private final String backUrl;

    private ProviderConfig(
            Provider provider,
            Environment environment,
            Duration timeout,
            String token,
            String publicKey,
            Map<String, String> defaultHeaders,
            String backUrl
    ) {
        this.provider = provider;
        this.environment = environment;
        this.timeout = timeout;
        this.token = token;
        this.publicKey = publicKey;
        this.defaultHeaders = defaultHeaders;
        this.backUrl = backUrl;
    }

    private static Map<String, String> defaultHeaders() {
        return Map.of(
                "Content-Type", "application/json",
                "Accept", "application/json"
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Configuração inteira das variáveis de ambiente; o provedor vem de {@code RECURRENCE_PROVIDER}. */
    public static ProviderConfig fromEnv() {
        return builder().fromEnv().build();
    }

    /** Configuração do provedor informado lida das variáveis {@code RECURRENCE_<PROVIDER>_*}. */
    public static ProviderConfig fromEnv(Provider provider) {
        return builder().provider(provider).fromEnv().build();
    }

    /** Provedor; pode ser {@code null} quando informado direto em {@code Recurrence.config(provider, ...)}. */
    public Provider getProvider() {
        return provider;
    }

    /** Ambiente; {@code SANDBOX} quando não informado. */
    public Environment getEnvironment() {
        return environment;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public String getToken() {
        return token;
    }

    /** Chave pública do provedor (no PagBank, a chave RSA usada para criptografar o cartão). */
    public String getPublicKey() {
        return publicKey;
    }

    public Map<String, String> getDefaultHeaders() {
        return defaultHeaders;
    }

    public String getBackUrl(){
        return backUrl;
    }

    public static class Builder {
        private Provider provider;
        private Environment environment;
        private Duration timeout;
        private String token;
        private String publicKey;
        private Map<String, String> defaultHeaders;
        private String backUrl;
        private boolean fromEnv;

        private Builder() {
        }

        /**
         * Completa os campos não declarados no código com as variáveis de ambiente,
         * na hora do {@link #build()}. A ordem das chamadas não importa: o valor do
         * código sempre ganha.
         */
        public Builder fromEnv() {
            this.fromEnv = true;
            return this;
        }

        public Builder provider(final Provider provider) {
            this.provider = provider;
            return this;
        }

        public Builder environment(final Environment environment) {
            this.environment = environment;
            return this;
        }

        public Builder timeout(final Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Define o token de autenticação do provedor.
         *
         * @param token token de autenticação do provedor
         * @return o builder atual
         */
        public Builder token(final String token) {
            this.token = token;
            return this;
        }

        /** Chave pública do provedor; no PagBank evita a consulta a {@code GET /public-keys}. */
        public Builder publicKey(final String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public Builder defaultHeaders(final Map<String, String> defaultHeaders) {
            this.defaultHeaders = defaultHeaders;
            return this;
        }

        /** Adiciona um header aos padrões ({@code Content-Type} e {@code Accept} JSON). */
        public Builder defaultHeader(final String name, final String value) {
            if (this.defaultHeaders == null) {
                this.defaultHeaders = new HashMap<>(ProviderConfig.defaultHeaders());
            } else {
                this.defaultHeaders = new HashMap<>(this.defaultHeaders);
            }
            this.defaultHeaders.put(name, value);
            return this;
        }

        public Builder backUrl(final String backUrl) {
            this.backUrl = backUrl;
            return this;
        }

        /** @deprecated use {@link #backUrl(String)}. */
        @Deprecated
        public Builder setBackUrl(final String backUrl){
            return backUrl(backUrl);
        }

        public ProviderConfig build() {
            Provider resolvedProvider = provider;
            Environment resolvedEnvironment = environment;
            Duration resolvedTimeout = timeout;
            String resolvedToken = token;
            String resolvedPublicKey = publicKey;
            String resolvedBackUrl = backUrl;

            if (fromEnv) {
                EnvironmentConfigLoader env = EnvironmentConfigLoader.system();
                if (resolvedProvider == null) resolvedProvider = env.provider();
                if (resolvedEnvironment == null) resolvedEnvironment = env.environment(resolvedProvider);
                if (resolvedTimeout == null) resolvedTimeout = env.timeout(resolvedProvider);
                if (resolvedToken == null) resolvedToken = env.get(resolvedProvider, TOKEN);
                if (resolvedPublicKey == null) resolvedPublicKey = env.get(resolvedProvider, PUBLIC_KEY);
                if (resolvedBackUrl == null) resolvedBackUrl = env.get(resolvedProvider, BACK_URL);
            }

            if (resolvedToken == null || resolvedToken.isBlank()) {
                throw new RecurrenceConfigurationException(resolvedProvider == null
                        ? CONFIG_TOKEN_REQUIRED_NO_PROVIDER
                        : String.format(CONFIG_TOKEN_REQUIRED, EnvironmentConfigLoader.variable(resolvedProvider, TOKEN)));
            }

            return new ProviderConfig(
                    resolvedProvider,
                    resolvedEnvironment == null ? Environment.SANDBOX : resolvedEnvironment,
                    resolvedTimeout == null ? DEFAULT_TIMEOUT : resolvedTimeout,
                    resolvedToken,
                    resolvedPublicKey,
                    defaultHeaders == null ? ProviderConfig.defaultHeaders() : defaultHeaders,
                    resolvedBackUrl
            );
        }
    }
}
