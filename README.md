# Recurrence Payment SDK

Biblioteca Java para abstrair provedores de pagamento recorrente (planos,
assinaturas, faturas e pagamentos) atrás de um único contrato, para que o
consumidor da lib não precise conhecer o provedor ativo.

## Status

Em desenvolvimento. O contrato (`com.recurrence.contract`) e o adaptador
PagBank estão em andamento; Mercado Pago ainda é um esqueleto
(`MercadoPagoProvider` retorna `null` em todo método). Consulte
`docs/modelo-canonico.md`, seção 12, para a lista de pendências conhecidas
antes de usar em produção.

## Uso

```java
Recurrence recurrence = Recurrence.config(
    Provider.PAGBANK,
    Environment.SANDBOX,
    ProviderConfig.builder()
        .token("seu-token")
        .timeout(Duration.ofSeconds(15))
        .build()
);

Plan plan = recurrence.plan().create(
    CreatePlanRequest.builder()
        .name("Plano mensal")
        .amount(5000, Currency.BRL)
        .interval(PlanIntervalType.MONTH, 1)
        .build()
);
```

## Documentação

- [`docs/modelo-canonico.md`](docs/modelo-canonico.md) — o contrato
  canônico: tipos, services e o que cada campo significa em cada provedor.
- [`docs/criterios.md`](docs/criterios.md) — os critérios usados para
  decidir o que entra no modelo canônico.

## Build

Gradle com toolchain Java/Kotlin 16 (`lib/build.gradle.kts`).

```
./gradlew build
```
