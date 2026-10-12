package com.recurrence.core;

import com.recurrence.contract.config.Provider;
import com.recurrence.contract.config.ProviderConfig;
import com.recurrence.infrastructure.HttpClientAdapter;

import java.util.Objects;

/**
 * Tudo o que um service precisa para falar com um provedor: identificação,
 * URL base, configuração, cliente HTTP e tradutor de erros.
 *
 * <p>Criado uma vez pelo {@code RecurrenceProvider} e compartilhado entre os services.</p>
 */
public record ProviderContext(
        Provider provider,
        String baseUrl,
        ProviderConfig config,
        HttpClientAdapter httpClient,
        ProviderErrorTranslator errorTranslator
) {
    public ProviderContext {
        Objects.requireNonNull(provider, "provider must not be null");
        Objects.requireNonNull(baseUrl, "baseUrl must not be null");
        Objects.requireNonNull(config, "config must not be null");
        Objects.requireNonNull(httpClient, "httpClient must not be null");
        Objects.requireNonNull(errorTranslator, "errorTranslator must not be null");
    }
}
