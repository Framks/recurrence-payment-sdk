package com.recurrence.constants;

public final class ErrorMessage {
    private ErrorMessage() {}

    public static final String USER_NOT_FOUND = "Usuário não encontrado";
    public static final String INVALID_TOKEN = "Token inválido";
    public static final String CONNECTION_ERROR = "Erro ao conectar ao servidor";
    public static final String PAGBANK_NOT_FOUND_TOKEN = "For PagBank Provider is necessary token";
    public static final String PAGBANK_AUTHENTICATION_HEADERS_TOKEN = "authentication for PagBank not has been send here, use token";
    public static final String PAGBANK_NOT_SUPPORT_BACK_URL = "Pagbank provider do not support back url: remove ProviderConfig.Builder.backUrl(...) and the environment variable RECURRENCE_PAGBANK_BACK_URL";
    public static final String MERCADO_PAGO_NOT_FOUND_TOKEN = "For Mercado Pago Provider is necessary token";
    public static final String MERCADO_PAGO_AUTHENTICATION_HEADERS_TOKEN = "authentication for Mercado Pago not has been send here, use token";
    public static final String ENV_VARIABLE_REQUIRED = "environment variable %s is required";
    public static final String ENV_VARIABLE_INVALID = "environment variable %s has invalid value '%s', expected %s";
    public static final String CONFIG_TOKEN_REQUIRED = "token is required: use ProviderConfig.Builder.token(...) or the environment variable %s";
    public static final String CONFIG_TOKEN_REQUIRED_NO_PROVIDER = "token is required: use ProviderConfig.Builder.token(...)";
    public static final String CONFIG_PROVIDER_REQUIRED = "provider is required: use ProviderConfig.Builder.provider(...) or the environment variable %s";
    public static final String CONFIG_PROVIDER_MISMATCH = "provider %s informed in Recurrence.config differs from provider %s of ProviderConfig";
    public static final String MERCADO_PAGO_BACK_URL_REQUIRED = "Mercado Pago provider requires back url (back_url of plans and subscriptions): use ProviderConfig.Builder.backUrl(...) or the environment variable RECURRENCE_MERCADO_PAGO_BACK_URL";
}
