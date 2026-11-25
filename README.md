# Recurrence Payment SDK

Biblioteca Java que coloca os provedores de pagamento recorrente **PagBank** e
**Mercado Pago** atrás de um contrato único: planos, assinaturas, faturas e pagamentos,
e tokenização de cartão. O seu código usa só os tipos da biblioteca; trocar de provedor
é trocar a configuração, sem reescrever a integração. O objetivo é reduzir o
*vendor lock-in*.

## Status

- Versão `v0.0.1-pre-avaliacao`, artefato de um TCC (Sistemas de Informação, UFC Quixadá).
- **Os dois provedores estão implementados**: PagBank e Mercado Pago implementam as 19
  operações do contrato, com as diferenças descritas em
  [`docs/compatibilidade.md`](docs/compatibilidade.md).
- Ainda não há testes automatizados. A avaliação de portabilidade nos *sandboxes* está
  em andamento.
- Meio de pagamento: só cartão de crédito.

## Requisitos

- Java 16 ou superior.
- Dependência em tempo de execução: Jackson 2.17 (`jackson-databind` e
  `jackson-datatype-jsr310`).
- Para compilar a biblioteca: um JDK 17 ou superior para rodar o Gradle (o wrapper é o
  9.7.1). O JDK 16 da *toolchain* é baixado pelo Gradle se não estiver instalado.

## Instalação

A biblioteca ainda não está publicada num repositório Maven. As duas formas abaixo foram
testadas.

### Opção 1: build composto (`includeBuild`)

Clone este repositório ao lado do seu projeto e, no seu `settings.gradle.kts`:

```kotlin
includeBuild("../recurrence-payment-sdk")
```

No seu `build.gradle.kts`:

```kotlin
dependencies {
    implementation("recurrence-payment-sdk:lib")
}
```

O Gradle compila a biblioteca junto com o seu projeto e traz o Jackson automaticamente.

### Opção 2: jar local

```bash
./gradlew jar    # gera lib/build/libs/lib.jar
```

Copie o jar para o seu projeto e declare também o Jackson, que não vai dentro do jar:

```kotlin
dependencies {
    implementation(files("libs/lib.jar"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")
}
```

## Quick start

Com `RECURRENCE_PROVIDER=PAGBANK` e `RECURRENCE_PAGBANK_TOKEN=<seu token>` no ambiente:

```java
Recurrence recurrence = Recurrence.config(ProviderConfig.fromEnv());

Plan plan = recurrence.plan().create(CreatePlanRequest.builder()
        .name("Plano mensal")
        .amount(4990, Currency.BRL)              // centavos: R$ 49,90
        .interval(PlanIntervalType.MONTH, 1)
        .build());

Subscription subscription = recurrence.subscription().create(CreateSubscriptionRequest.builder()
        .planId(plan.getId())
        .referenceId("PEDIDO-2026-001")
        .customer(CustomerRequest.builder()
                .name("Maria Silva")
                .email("maria@exemplo.com")
                .taxId("12345678909")
                .phone(Phone.builder().country("55").area("88").number("999999999").build())
                .build())
        .paymentMethod(BillingInfo.builder()
                .type(PaymentMethodType.CREDIT_CARD)
                .card(Card.builder().token(tokenDoCartao).securityCode("123").build())
                .build())
        .build());
```

`tokenDoCartao` é o cartão tokenizado pelo SDK JS do provedor no frontend (veja o aviso
abaixo).

## Trocar de provedor

Só a configuração muda. O código acima roda no Mercado Pago com:

```bash
RECURRENCE_PROVIDER=MERCADO_PAGO
RECURRENCE_MERCADO_PAGO_TOKEN=<access token>
RECURRENCE_MERCADO_PAGO_BACK_URL=https://minha-loja.com/assinatura/retorno
```

O Mercado Pago exige uma URL de retorno (`BACK_URL`); o PagBank recusa a configuração
se ela vier preenchida. O que cada provedor aceita ou recusa está em
[`docs/compatibilidade.md`](docs/compatibilidade.md). A principal diferença: no sandbox do
Mercado Pago a troca de plano de uma assinatura responde com sucesso mas não é aplicada.

## Tokenização de cartão e PCI-DSS

> **Aviso:** `recurrence.card().tokenize(...)` recebe número e CVV do cartão no backend,
> o que coloca a sua integração no **escopo completo do PCI-DSS**. Em produção, gere o
> token no frontend com o SDK JS do provedor (PagBank `PagSeguro.encryptCard`, Mercado
> Pago `card_token`) e envie só o token ao backend. Use `tokenize` em testes no
> *sandbox* ou se o seu ambiente for certificado.

## Documentação

- [`docs/guia-de-uso.md`](docs/guia-de-uso.md): configuração, exemplos de todas as
  operações, paginação, filtros, idempotência e erros.
- [`docs/modelo-canonico.md`](docs/modelo-canonico.md): cada tipo do contrato e a
  conversão de e para cada provedor.
- [`docs/compatibilidade.md`](docs/compatibilidade.md): as 19 operações nos dois
  provedores.
- [`docs/arquitetura.md`](docs/arquitetura.md): pacotes, fluxo de uma chamada e
  decisões de projeto.
- [`docs/novo-provedor.md`](docs/novo-provedor.md): como adicionar um terceiro provedor.
- [`docs/criterios.md`](docs/criterios.md): critérios para um campo entrar no contrato.
- [`CHANGELOG.md`](CHANGELOG.md): histórico das versões do contrato.
- Javadoc: `./gradlew javadoc` (saída em `lib/build/docs/javadoc/`).

## Build

```bash
./gradlew build
```
