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
 * <p>{@code backUrl} é o único campo de um provedor só: o Mercado Pago exige
 * {@code back_url} na criação de planos e assinaturas e recusa a configuração sem
 * ele; o PagBank recusa a configuração se ele vier preenchido. Fica aqui como
 * exceção justificada, validada na hora de configurar.</p>
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

    /**
     * Inicia uma configuração declarada no código.
     *
     * @return um builder vazio
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Configuração inteira das variáveis de ambiente; o provedor vem de {@code RECURRENCE_PROVIDER}.
     *
     * @return a configuração lida do ambiente
     * @throws RecurrenceConfigurationException se {@code RECURRENCE_PROVIDER} ou o token estiverem
     *         ausentes, ou se alguma variável tiver valor inválido
     */
    public static ProviderConfig fromEnv() {
        return builder().fromEnv().build();
    }

    /**
     * Configuração do provedor informado lida das variáveis {@code RECURRENCE_<PROVIDER>_*}.
     * {@code RECURRENCE_PROVIDER} é ignorada.
     *
     * @param provider provedor cujas variáveis serão lidas
     * @return a configuração lida do ambiente
     * @throws RecurrenceConfigurationException se o token estiver ausente ou alguma variável tiver valor inválido
     */
    public static ProviderConfig fromEnv(Provider provider) {
        return builder().provider(provider).fromEnv().build();
    }

    /**
     * Provedor configurado.
     *
     * @return o provedor; pode ser {@code null} quando informado direto em {@code Recurrence.config(provider, ...)}
     */
    public Provider getProvider() {
        return provider;
    }

    /**
     * Ambiente do provedor.
     *
     * @return o ambiente; {@code SANDBOX} quando não informado
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Timeout padrão das chamadas, sobrescrito por {@code RequestOptions.timeout} quando informado.
     *
     * @return o timeout; 15 segundos quando não informado
     */
    public Duration getTimeout() {
        return timeout;
    }

    /**
     * Token de autenticação, enviado como {@code Authorization: Bearer <token>}.
     *
     * @return o token, nunca em branco
     */
    public String getToken() {
        return token;
    }

    /**
     * Chave pública do provedor (no PagBank, a chave RSA usada para criptografar o cartão).
     *
     * @return a chave em Base64 (X.509), ou {@code null} quando não informada
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Headers enviados em todas as chamadas.
     *
     * @return os headers; por padrão {@code Content-Type} e {@code Accept} com {@code application/json}
     */
    public Map<String, String> getDefaultHeaders() {
        return defaultHeaders;
    }

    /**
     * URL de retorno do Mercado Pago ({@code back_url} de planos e assinaturas).
     *
     * @return a URL, ou {@code null} quando não informada (obrigatória no Mercado Pago, proibida no PagBank)
     */
    public String getBackUrl(){
        return backUrl;
    }

    /**
     * Monta um {@link ProviderConfig}. Campos não informados recebem o valor das
     * variáveis de ambiente (se {@link #fromEnv()} for chamado) ou o padrão.
     */
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
         *
         * @return o builder atual
         */
        public Builder fromEnv() {
            this.fromEnv = true;
            return this;
        }

        /**
         * Define o provedor.
         *
         * @param provider provedor a usar
         * @return o builder atual
         */
        public Builder provider(final Provider provider) {
            this.provider = provider;
            return this;
        }

        /**
         * Define o ambiente.
         *
         * @param environment {@code SANDBOX} (padrão) ou {@code PRODUCTION}
         * @return o builder atual
         */
        public Builder environment(final Environment environment) {
            this.environment = environment;
            return this;
        }

        /**
         * Define o timeout padrão das chamadas.
         *
         * @param timeout timeout de cada requisição HTTP; padrão de 15 segundos
         * @return o builder atual
         */
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

        /**
         * Define a chave pública do provedor; no PagBank evita a consulta a {@code GET /public-keys}.
         *
         * @param publicKey chave RSA em Base64 (X.509)
         * @return o builder atual
         */
        public Builder publicKey(final String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        /**
         * Substitui todos os headers padrão.
         *
         * <p>Não inclua {@code Authorization}: os provedores recusam a configuração
         * e usam sempre o {@link #token(String)}.</p>
         *
         * @param defaultHeaders headers enviados em todas as chamadas
         * @return o builder atual
         */
        public Builder defaultHeaders(final Map<String, String> defaultHeaders) {
            this.defaultHeaders = defaultHeaders;
            return this;
        }

        /**
         * Adiciona um header aos padrões ({@code Content-Type} e {@code Accept} JSON).
         *
         * @param name  nome do header
         * @param value valor do header
         * @return o builder atual
         */
        public Builder defaultHeader(final String name, final String value) {
            if (this.defaultHeaders == null) {
                this.defaultHeaders = new HashMap<>(ProviderConfig.defaultHeaders());
            } else {
                this.defaultHeaders = new HashMap<>(this.defaultHeaders);
            }
            this.defaultHeaders.put(name, value);
            return this;
        }

        /**
         * Define a URL de retorno exigida pelo Mercado Pago. Não informe para o PagBank.
         *
         * @param backUrl URL para onde o Mercado Pago redireciona o assinante
         * @return o builder atual
         */
        public Builder backUrl(final String backUrl) {
            this.backUrl = backUrl;
            return this;
        }

        /**
         * Mesmo que {@link #backUrl(String)}.
         *
         * @param backUrl URL para onde o Mercado Pago redireciona o assinante
         * @return o builder atual
         * @deprecated use {@link #backUrl(String)}.
         */
        @Deprecated
        public Builder setBackUrl(final String backUrl){
            return backUrl(backUrl);
        }

        /**
         * Resolve os campos (código, depois variáveis de ambiente, depois padrão) e cria a configuração.
         *
         * @return a configuração
         * @throws RecurrenceConfigurationException se o token não for informado ou uma variável de
         *         ambiente tiver valor inválido
         */
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
