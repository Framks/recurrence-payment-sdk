package com.recurrence.pagbank;

public enum PagBankEnvironment {

    SANDBOX("https://sandbox.api.assinaturas.pagseguro.com"),
    PRODUCTION("https://api.assinaturas.pagseguro.com");

    private final String host;

    PagBankEnvironment(final String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}
