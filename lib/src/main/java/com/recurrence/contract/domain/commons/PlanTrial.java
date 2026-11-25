package com.recurrence.contract.domain.commons;

// Como isso esta associado literalmente ao plano não seria melhor deixar dentro da classe Plan ou por ser usado na request é melhor ter um para cada?
public record PlanTrial(
        Integer days
) { }
