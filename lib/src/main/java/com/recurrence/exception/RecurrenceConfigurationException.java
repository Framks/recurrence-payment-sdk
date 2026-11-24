package com.recurrence.exception;

/**
 * Configuração inválida para o provedor escolhido (token ausente, campo não
 * suportado, etc.). Lançada ao montar o provedor, antes de qualquer chamada.
 */
public class RecurrenceConfigurationException extends RecurrenceException {

    public RecurrenceConfigurationException(String message) {
        super(message);
    }
}
