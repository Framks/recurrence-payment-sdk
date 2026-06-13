package com.recurrence.pagbank;

import com.recurrence.contract.config.Environment;
import com.recurrence.contract.config.ProviderConfig;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class PagbankConfig {

    private final String host;
    private final Duration timeout;
    private final String token;
    private final Map<String, String> defaultHeaders;

    PagbankConfig(
            Environment environment,
            ProviderConfig config
    ){
        host = switch (environment){
            case SANDBOX -> "https://sandbox.api.assinaturas.pagseguro.com";
            case PRODUCTION -> "https://api.assinaturas.pagseguro.com";
        };
        token = config.getToken();
        timeout = config.getTimeout();
        defaultHeaders = config.getDefaultHeaders();
    }

    public String getHost() {
        return host;
    }

    public Map<String, String> getDefaultHeaders(){
        return defaultHeaders;
    }

    public Duration getTimeout(){
        return timeout;
    }

    public String getToken(){
        return token;
    }

    public Map<String, String> fullHeaders(){
        Map<String, String> headers = new HashMap<>(defaultHeaders);
        headers.put("Authorization", "Bearer " + token);

        return Map.copyOf(headers);
    }
}
