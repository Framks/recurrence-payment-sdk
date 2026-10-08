package com.recurrence.constants;

public final class ErrorMessage {
    private ErrorMessage() {}

    public static final String USER_NOT_FOUND = "Usuário não encontrado";
    public static final String INVALID_TOKEN = "Token inválido";
    public static final String CONNECTION_ERROR = "Erro ao conectar ao servidor";
    public static final String PAGBANK_NOT_FOUND_TOKEN = "For PagBank Provider is necessary token";
    public static final String PAGBANK_AUTHENTICATION_HEADERS_TOKEN = "authentication for PagBank not has been send here, use token";
    public static final String PAGBANK_NOT_SUPPORT_BACK_URL = "Pagbank provider do not support back url";
    public static final String MERCADO_PAGO_NOT_FOUND_TOKEN = "For Mercado Pago Provider is necessary token";
    public static final String MERCADO_PAGO_AUTHENTICATION_HEADERS_TOKEN = "authentication for Mercado Pago not has been send here, use token";
    public static final String MERCADO_PAGO_BACK_URL_REQUIRED = "Mercado Pago provider requires back url (back_url of plans and subscriptions)";
}
