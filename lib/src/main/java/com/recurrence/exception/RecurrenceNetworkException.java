package com.recurrence.exception;

/**
 * Não foi possível obter resposta do provedor (falha de IO, timeout ou
 * thread interrompida). Não há status HTTP associado.
 */
public class RecurrenceNetworkException extends RecurrenceException {

    private final String url;

    public RecurrenceNetworkException(String url, String message, Throwable cause) {
        super(message + " (" + url + ")", cause);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
