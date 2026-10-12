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

## Configuração

O provedor pode ser configurado no código, pelas variáveis de ambiente ou pelas
duas formas juntas. Quando um campo vem dos dois lados, **o valor declarado no
código ganha**.

```java
// 1. Só variáveis de ambiente
Recurrence recurrence = Recurrence.config(ProviderConfig.fromEnv());

// 2. Só código
Recurrence recurrence = Recurrence.config(
    Provider.PAGBANK,
    Environment.SANDBOX,
    ProviderConfig.builder()
        .token("seu-token")
        .timeout(Duration.ofSeconds(15))
        .build()
);

// 3. Variáveis de ambiente com campos sobrescritos no código
Recurrence recurrence = Recurrence.config(
    ProviderConfig.builder()
        .provider(Provider.PAGBANK)   // ganha de RECURRENCE_PROVIDER
        .fromEnv()
        .timeout(Duration.ofSeconds(30))
        .build()
);
```

### Variáveis de ambiente

O provedor vem de `RECURRENCE_PROVIDER`; os demais campos usam o nome do
provedor (`PAGBANK` ou `MERCADO_PAGO`), o que permite configurar os dois no
mesmo processo.

| Variável | Campo | PagBank | Mercado Pago |
|---|---|---|---|
| `RECURRENCE_PROVIDER` | provedor | `PAGBANK` | `MERCADO_PAGO` |
| `RECURRENCE_<P>_ENVIRONMENT` | `environment` | padrão `SANDBOX` | padrão `SANDBOX` |
| `RECURRENCE_<P>_TOKEN` | `token` | obrigatório | obrigatório (access token) |
| `RECURRENCE_<P>_PUBLIC_KEY` | `publicKey` | opcional: chave RSA do cartão; sem ela, consulta `GET /public-keys` | opcional, ainda não usada |
| `RECURRENCE_<P>_BACK_URL` | `backUrl` | não suportado | obrigatório |
| `RECURRENCE_<P>_TIMEOUT` | `timeout` (segundos) | padrão 15 | padrão 15 |

Variável ausente ou com valor inválido gera `RecurrenceConfigurationException`
com o nome da variável.

## Uso

```java
Plan plan = recurrence.plan().create(
    CreatePlanRequest.builder()
        .name("Plano mensal")
        .amount(5000, Currency.BRL)
        .interval(PlanIntervalType.MONTH, 1)
        .build()
);
```

### Tokenização do cartão no backend

`card().tokenize(...)` devolve o `Card` pronto para usar como meio de
pagamento. No PagBank o cartão é criptografado localmente com a chave pública
da conta (`GET /public-keys`, que precisa existir); no Mercado Pago é criado um
`card_token` de uso único. `holderTaxId` é obrigatório no Mercado Pago e
ignorado pelo PagBank.

```java
Card card = recurrence.card().tokenize(
    TokenizeCardRequest.builder()
        .number("5555666677778884")
        .securityCode("123")
        .expMonth(12)
        .expYear(2030)
        .holderName("Joao Pereira Azul")
        .holderTaxId("12345678909")
        .build()
);
```

> **PCI-DSS:** receber número e CVV no backend coloca a integração no escopo
> completo do PCI-DSS. Em produção, prefira gerar o token no frontend com o SDK
> JS do provedor e use este método apenas em testes ou em ambiente certificado.

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
