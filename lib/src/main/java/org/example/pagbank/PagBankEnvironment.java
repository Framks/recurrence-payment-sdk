package org.example.pagbank;

public enum PagBankEnvironment {

    SANDBOX("https://sandbox.api.assinaturas.pagseguro.com"),
    PRODUCTION("http://production");

    private String host;

    PagBankEnvironment(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}
