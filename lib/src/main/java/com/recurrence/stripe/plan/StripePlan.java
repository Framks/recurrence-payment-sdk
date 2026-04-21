package com.recurrence.stripe.plan;

public record StripePlan (
    String id,
    String object,
    Boolean active,
    Integer amount,    ///  obrigatorio ao criar
    String amountDecimal,
    String billingScheme,
    String created,
    String currency,   ///  obrigatorio ao criar
    String interval,   ///  obrigatorio ao criar
    Integer intervalCount,
    Boolean livemode,
    String metadata,
    String nickname,
    String product,   ///  obrigatorio ao criar
    String tiersMode, ///  obrigatorio se billingScheme for tiered
    String transformUsage,  // mudar para object
    String trialPeriodDays,
    String usageType
) { }
