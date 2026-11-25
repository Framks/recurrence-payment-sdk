package com.recurrence.core;

import com.recurrence.contract.config.Environment;
import com.recurrence.contract.config.Provider;
import com.recurrence.exception.RecurrenceConfigurationException;

import java.time.Duration;
import java.util.Arrays;
import java.util.function.Function;

import static com.recurrence.constants.ErrorMessage.ENV_VARIABLE_INVALID;
import static com.recurrence.constants.ErrorMessage.ENV_VARIABLE_REQUIRED;

/**
 * Lê a configuração do provedor das variáveis de ambiente.
 *
 * <p>O provedor vem de {@code RECURRENCE_PROVIDER}; os demais campos usam o nome
 * do provedor no meio ({@code RECURRENCE_<PROVIDER>_<CAMPO>}), o que permite
 * configurar mais de um provedor no mesmo processo:</p>
 *
 * <pre>
 * RECURRENCE_PROVIDER=PAGBANK
 * RECURRENCE_PAGBANK_ENVIRONMENT=SANDBOX
 * RECURRENCE_PAGBANK_TOKEN=...
 * RECURRENCE_PAGBANK_PUBLIC_KEY=...
 * RECURRENCE_PAGBANK_TIMEOUT=15
 * RECURRENCE_MERCADO_PAGO_TOKEN=...
 * RECURRENCE_MERCADO_PAGO_BACK_URL=https://...
 * </pre>
 *
 * <p>Variável ausente ou em branco devolve {@code null}; valor que não pode ser
 * convertido lança {@link RecurrenceConfigurationException} com o nome da variável.</p>
 */
public final class EnvironmentConfigLoader {

    public static final String PREFIX = "RECURRENCE_";
    public static final String PROVIDER = PREFIX + "PROVIDER";
    public static final String ENVIRONMENT = "ENVIRONMENT";
    public static final String TOKEN = "TOKEN";
    public static final String PUBLIC_KEY = "PUBLIC_KEY";
    public static final String BACK_URL = "BACK_URL";
    public static final String TIMEOUT = "TIMEOUT";

    private final Function<String, String> source;

    EnvironmentConfigLoader(Function<String, String> source) {
        this.source = source;
    }

    public static EnvironmentConfigLoader system() {
        return new EnvironmentConfigLoader(System::getenv);
    }

    /** Nome da variável de um campo do provedor, ex.: {@code RECURRENCE_PAGBANK_TOKEN}. */
    public static String variable(Provider provider, String field) {
        return PREFIX + provider.name() + "_" + field;
    }

    /** Provedor de {@code RECURRENCE_PROVIDER}, obrigatório quando não informado no código. */
    public Provider provider() {
        String value = read(PROVIDER);
        if (value == null) {
            throw new RecurrenceConfigurationException(String.format(ENV_VARIABLE_REQUIRED, PROVIDER));
        }
        return parseEnum(Provider.class, PROVIDER, value);
    }

    public Environment environment(Provider provider) {
        String name = variable(provider, ENVIRONMENT);
        String value = read(name);
        return value == null ? null : parseEnum(Environment.class, name, value);
    }

    public String get(Provider provider, String field) {
        return read(variable(provider, field));
    }

    /** Timeout em segundos. */
    public Duration timeout(Provider provider) {
        String name = variable(provider, TIMEOUT);
        String value = read(name);
        if (value == null) return null;
        try {
            long seconds = Long.parseLong(value);
            if (seconds <= 0) throw new NumberFormatException();
            return Duration.ofSeconds(seconds);
        } catch (NumberFormatException e) {
            throw new RecurrenceConfigurationException(
                    String.format(ENV_VARIABLE_INVALID, name, value, "a positive number of seconds"));
        }
    }

    private String read(String name) {
        String value = source.apply(name);
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static <E extends Enum<E>> E parseEnum(Class<E> type, String name, String value) {
        try {
            return Enum.valueOf(type, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RecurrenceConfigurationException(
                    String.format(ENV_VARIABLE_INVALID, name, value, Arrays.toString(type.getEnumConstants())));
        }
    }
}
