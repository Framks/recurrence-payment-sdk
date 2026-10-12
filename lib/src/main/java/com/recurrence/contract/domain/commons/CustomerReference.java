package com.recurrence.contract.domain.commons;

/**
 * Referência resumida ao assinante, usada dentro de assinaturas, faturas e pagamentos.
 */
public record CustomerReference(
        String id,
        String name,
        String email
) { }
