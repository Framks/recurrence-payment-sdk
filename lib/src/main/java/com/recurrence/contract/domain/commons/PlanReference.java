package com.recurrence.contract.domain.commons;

/**
 * Referência resumida a um plano, usada dentro de assinaturas e faturas.
 * Para o plano completo, use {@code PlanService.get(id)}.
 */
public record PlanReference(
        String id,
        String name
) { }
