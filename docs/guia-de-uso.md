# Guia de uso

Este guia mostra como configurar a biblioteca e usar cada operação do contrato. Todos
os exemplos compilam contra o contrato atual. Os imports vêm de `com.recurrence.contract`
(e subpacotes) e de `com.recurrence.exception`.

Sumário:

1. [Configuração](#1-configuração)
2. [Planos](#2-planos-planservice)
3. [Assinaturas](#3-assinaturas-subscriptionservice)
4. [Faturas e pagamentos](#4-faturas-e-pagamentos-invoiceservice)
5. [Cartão](#5-cartão-cardservice)
6. [Paginação](#6-paginação)
7. [Filtros](#7-filtros)
8. [`RequestOptions`: idempotência e timeout](#8-requestoptions-idempotência-e-timeout)
9. [Tratamento de erros](#9-tratamento-de-erros)
10. [Valores `UNKNOWN`](#10-valores-unknown)

## 1. Configuração

Tudo começa em `Recurrence`, que recebe um `ProviderConfig` e devolve os services do
provedor configurado. A configuração pode vir do código, das variáveis de ambiente ou das
duas coisas juntas. Quando um campo vem dos dois lados, **o valor do código ganha**.

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
                .build());

// 3. Variáveis de ambiente com campos sobrescritos no código
Recurrence recurrence = Recurrence.config(ProviderConfig.builder()
        .provider(Provider.MERCADO_PAGO)   // ganha de RECURRENCE_PROVIDER
        .fromEnv()
        .timeout(Duration.ofSeconds(30))   // ganha de RECURRENCE_MERCADO_PAGO_TIMEOUT
        .build());
```

Outras variações:

```java
// Variáveis de um provedor específico, ignorando RECURRENCE_PROVIDER
Recurrence recurrence = Recurrence.config(ProviderConfig.fromEnv(Provider.PAGBANK));

// Mercado Pago exige backUrl
Recurrence recurrence = Recurrence.config(
        Provider.MERCADO_PAGO,
        Environment.SANDBOX,
        ProviderConfig.builder()
                .token("APP_USR-...")
                .backUrl("https://minha-loja.com/assinatura/retorno")
                .build());

// Header extra em todas as chamadas
ProviderConfig config = ProviderConfig.builder()
        .provider(Provider.PAGBANK)
        .token("seu-token")
        .defaultHeader("X-Correlation-Id", "abc-123")
        .build();
```

`Recurrence.config(provider, environment, config)` usa o provedor e o ambiente passados
como argumento. Se `config` já tiver outro provedor, a chamada lança
`RecurrenceConfigurationException`.

### 1.1 Variáveis de ambiente

Lidas por `core.EnvironmentConfigLoader`. O provedor vem de `RECURRENCE_PROVIDER`; os
demais campos levam o nome do provedor no meio (`<P>` = `PAGBANK` ou `MERCADO_PAGO`), o que
permite configurar os dois no mesmo processo.

| Variável | Campo do `ProviderConfig` | Valores | PagBank | Mercado Pago |
|---|---|---|---|---|
| `RECURRENCE_PROVIDER` | `provider` | `PAGBANK`, `MERCADO_PAGO` (maiúsculas ou minúsculas) | obrigatória em `fromEnv()` | obrigatória em `fromEnv()` |
| `RECURRENCE_<P>_ENVIRONMENT` | `environment` | `SANDBOX`, `PRODUCTION` | padrão `SANDBOX` | padrão `SANDBOX` (a URL é a mesma; o ambiente depende da credencial) |
| `RECURRENCE_<P>_TOKEN` | `token` | texto | obrigatória | obrigatória (access token) |
| `RECURRENCE_<P>_PUBLIC_KEY` | `publicKey` | chave RSA em Base64 | opcional; sem ela, `card().tokenize` consulta `GET /public-keys` | não usada |
| `RECURRENCE_<P>_BACK_URL` | `backUrl` | URL | **proibida** | **obrigatória** |
| `RECURRENCE_<P>_TIMEOUT` | `timeout` | inteiro positivo, em segundos | padrão 15 | padrão 15 |

Variável ausente ou em branco conta como não informada; os valores são lidos sem espaços
nas pontas. Valor que não pode ser convertido lança `RecurrenceConfigurationException`
com o nome da variável, por exemplo
`environment variable RECURRENCE_PAGBANK_TIMEOUT has invalid value 'abc', expected a positive number of seconds`.

### 1.2 Padrões e validações da configuração

| Campo | Padrão | Validação |
|---|---|---|
| `environment` | `SANDBOX` | — |
| `timeout` | 15 segundos | — |
| `token` | — | obrigatório; sem ele, `build()` lança `RecurrenceConfigurationException` |
| `defaultHeaders` | `Content-Type` e `Accept` = `application/json` | não pode conter `Authorization`: o token é enviado como `Authorization: Bearer <token>` |
| `backUrl` | — | obrigatório no Mercado Pago (`MERCADO_PAGO_BACK_URL_REQUIRED`), proibido no PagBank (`PAGBANK_NOT_SUPPORT_BACK_URL`) |

As validações do provedor rodam em `Recurrence.config(...)`, antes de qualquer chamada.

## 2. Planos (`PlanService`)

```java
// criar
Plan plan = recurrence.plan().create(CreatePlanRequest.builder()
        .name("Plano anual")
        .amount(49900, Currency.BRL)              // centavos: R$ 499,00
        .interval(PlanIntervalType.MONTH, 12)
        .billingCycles(3)                         // 3 cobranças; null = sem limite
        .trial(7)                                 // 7 dias grátis
        .paymentMethods(List.of(PaymentMethodType.CREDIT_CARD))
        .build());

// consultar
Plan same = recurrence.plan().get(plan.getId());

// listar
PageResponse<Plan> page = recurrence.plan().list(
        PageOptions.of(0, 20),
        PlanFilter.builder().status(PlanStatus.ACTIVE).build());
for (Plan p : page.getData()) {
    System.out.println(p.getName() + " " + p.getAmount().value());
}

// atualizar (só os campos informados)
Plan updated = recurrence.plan().update(plan.getId(), UpdatePlanRequest.builder()
        .name("Plano anual (promo)")
        .amount(39900, Currency.BRL)
        .build());

// inativar e reativar
recurrence.plan().deactivate(plan.getId());
recurrence.plan().activate(plan.getId());
```

Obrigatórios na criação: `name`, `amount` e `interval`. Sem `paymentMethods`, o plano
aceita só cartão. Detalhes de cada campo em [`modelo-canonico.md`](modelo-canonico.md#51-createplanrequest-e-updateplanrequest).

## 3. Assinaturas (`SubscriptionService`)

```java
// criar
Subscription s = recurrence.subscription().create(
        CreateSubscriptionRequest.builder()
                .planId(planId)
                .referenceId("PEDIDO-2026-002")
                .amount(3990, Currency.BRL)       // opcional: sobrescreve o valor do plano
                .customer(CustomerRequest.builder()
                        .name("Maria Silva")
                        .email("maria@exemplo.com")
                        .taxId("12345678909")
                        .phone(Phone.builder().country("55").area("88").number("999999999").build())
                        .build())
                .paymentMethod(BillingInfo.builder()
                        .type(PaymentMethodType.CREDIT_CARD)
                        .card(card)               // veja a seção 5
                        .build())
                .build(),
        RequestOptions.builder().idempotencyKey(UUID.randomUUID().toString()).build());

// consultar
Subscription same = recurrence.subscription().get(s.getId());
System.out.println(same.getPlan().id() + " " + same.getCustomer().email() + " " + same.getNextInvoiceAt());

// listar
PageResponse<Subscription> page = recurrence.subscription().list(
        PageOptions.of(0, 10),
        SubscriptionFilter.builder()
                .status(SubscriptionStatus.ACTIVE)
                .referenceId("PEDIDO-2026-002")
                .build());
```

Campos exigidos na criação:

| Campo | PagBank | Mercado Pago |
|---|---|---|
| `planId` | obrigatório | obrigatório |
| `referenceId` | obrigatório | opcional |
| `customer.email` | obrigatório | obrigatório |
| `customer.name`, `customer.taxId`, `customer.phone` | obrigatórios | ignorados |
| `paymentMethod` com `card.token` | obrigatório | obrigatório |
| `card.securityCode` | obrigatório | ignorado |

Para o código funcionar nos dois provedores sem mudança, preencha sempre todos os campos
da tabela.

### 3.1 Atualizar

Informe só o que muda: valor, cartão ou plano. O método devolve a assinatura consultada
de novo depois da alteração.

```java
// valor
recurrence.subscription().update(s.getId(), UpdateSubscriptionRequest.builder()
        .amount(4500, Currency.BRL)
        .build());

// cartão
recurrence.subscription().update(s.getId(), UpdateSubscriptionRequest.builder()
        .paymentMethod(BillingInfo.builder()
                .type(PaymentMethodType.CREDIT_CARD)
                .card(newCard)
                .build())
        .build());

// plano (no sandbox do Mercado Pago a troca não é aplicada; confira getPlan() no retorno)
recurrence.subscription().update(s.getId(), UpdateSubscriptionRequest.builder()
        .planId("OUTRO_PLANO")
        .build());
```

- **Troca de plano**: o SDK envia o pedido aos dois provedores e devolve a assinatura
  consultada de novo. No sandbox do Mercado Pago o provedor responde 2xx e mantém o
  plano antigo, então confira `getPlan().id()` na assinatura devolvida. Se o provedor
  recusar a troca, o erro chega como `RecurrenceApiException`.
- **Troca de cartão** no PagBank vai para o assinante (`PUT /customers/{id}/billing_info`)
  e exige `card.securityCode`.

### 3.2 Suspender, reativar e cancelar

```java
recurrence.subscription().suspend(s.getId());
recurrence.subscription().activate(s.getId());
recurrence.subscription().cancel(s.getId());
```

## 4. Faturas e pagamentos (`InvoiceService`)

Uma fatura é a cobrança de um ciclo; um pagamento é uma tentativa de cobrar a fatura.

```java
// faturas de uma assinatura
PageResponse<Invoice> invoices = recurrence.invoice()
        .listInvoiceBySubscription(subscriptionId, PageOptions.of(0, 10));

// uma fatura
Invoice invoice = recurrence.invoice().getInvoice(invoices.getData().get(0).getId());
System.out.println(invoice.getStatus() + " " + invoice.getAmount().value());

// tentativas de cobrança da fatura
PageResponse<Payment> attempts = recurrence.invoice().listPayments(invoice.getId());

// um pagamento
Payment payment = recurrence.invoice().getPayment(attempts.getData().get(0).getId());

// pagamentos da conta, com filtro
PageResponse<Payment> approved = recurrence.invoice().listPayments(
        PageOptions.of(0, 50),
        PaymentFilter.builder()
                .status(PaymentStatus.APPROVED)
                .createdFrom(OffsetDateTime.now().minusDays(30))
                .createdUntil(OffsetDateTime.now())
                .build());
```

Diferenças entre os provedores:

| Operação | PagBank | Mercado Pago |
|---|---|---|
| `listPayments(invoiceId, filtro)` | `GET /invoices/{id}/payments`, com o filtro na query | busca a fatura e depois o pagamento dela; devolve zero ou um item e aplica o filtro em memória |
| `listPayments(pageOptions, filtro)` | `GET /payments` | `GET /v1/payments/search`, do mais recente para o mais antigo; **inclui pagamentos que não são de assinatura** |
| `Invoice.occurrence`, `Invoice.plan` | preenchidos | `null` |
| `Payment.invoiceId` | preenchido | `null`, exceto em `listPayments(invoiceId)` |
| `Payment.amount` | valor da fatura (o pagamento do PagBank não traz valor) | valor do pagamento |

## 5. Cartão (`CardService`)

O caminho recomendado é gerar o token **no frontend**, com o SDK JS do provedor ativo
(PagBank: `PagSeguro.encryptCard`; Mercado Pago: `card_token`), e montar o `Card` no backend:

```java
Card card = Card.builder()
        .token("TOKEN_GERADO_NO_FRONTEND")
        .securityCode("123")    // exigido pelo PagBank; ignorado pelo Mercado Pago
        .build();
```

Para testes no sandbox, ou se o seu ambiente for certificado PCI-DSS, dá para tokenizar
no backend:

```java
Card card = recurrence.card().tokenize(TokenizeCardRequest.builder()
        .number("5555666677778884")
        .securityCode("123")
        .expMonth(12)
        .expYear(2030)
        .holderName("Joao Pereira Azul")
        .holderTaxId("12345678909")     // obrigatório no Mercado Pago
        .build());
```

O `Card` devolvido já serve para `BillingInfo`. No PagBank ele traz o cartão criptografado
e o CVV; no Mercado Pago traz o `card_token`, de uso único e validade curta (gere um novo
para cada assinatura). No PagBank a conta precisa ter uma chave pública criada; sem ela,
`tokenize` falha com `RecurrenceApiException`.

> **PCI-DSS:** receber número e CVV do cartão no backend coloca a integração no escopo
> completo do PCI-DSS.

## 6. Paginação

As listagens recebem `PageOptions` e devolvem `PageResponse<T>`.

```java
PageOptions page = PageOptions.builder().offset(0).limit(20).build();
// ou PageOptions.of(0, 20)
```

| Campo | Regra |
|---|---|
| `offset` | opcional, a partir de zero; negativo lança `IllegalArgumentException` no `build()` |
| `limit` | opcional, maior ou igual a 1; menor lança `IllegalArgumentException` no `build()` |

Campos nulos (ou `pageOptions` nulo) deixam o padrão do provedor. Os dois provedores
recebem os parâmetros `offset` e `limit` na query.

A resposta tem `getData()` (os itens) e `getPage()`, um `PageInfo(offset, limit, total)`
copiado do `result_set` do PagBank ou do `paging` do Mercado Pago. Se o provedor não
devolver esse bloco, `PageInfo` fica com `offset = 0` e `limit = total =` quantidade de itens.

`listPayments(invoiceId, ...)` não recebe `PageOptions`.

## 7. Filtros

Filtro `null` não filtra. Os filtros só têm campos que existem nos dois provedores.
Quando um valor não tem equivalente no provedor ativo, a chamada é recusada com
`RecurrenceValidationException` antes da rede, em vez de devolver uma busca vazia.

### 7.1 `PlanFilter`

| Campo | PagBank | Mercado Pago |
|---|---|---|
| `status` | `status=ACTIVE` ou `INACTIVE` | `status=active`; `INACTIVE` vira `status=cancelled` |
| `query` | `reference_id` | `q` |

### 7.2 `SubscriptionFilter`

| Campo | PagBank | Mercado Pago |
|---|---|---|
| `status` | `status=<nome do enum>` (todos os valores) | `ACTIVE` → `authorized`, `PENDING` → `pending`, `SUSPENDED` → `paused`, `CANCELED` → `cancelled`; **`OVERDUE` e `EXPIRED` são recusados** |
| `query` | `q` | `q` |
| `referenceId` | `reference_id` | `external_reference` |

### 7.3 `PaymentFilter`

| Campo | PagBank | Mercado Pago |
|---|---|---|
| `status` | `status=<nome do enum>`; `DECLINED` vira `DENIED` | `APPROVED` → `approved`, `PENDING` → `pending`, `DECLINED` → `rejected`, `REFUNDED` → `refunded`; **`UNPAID` é recusado** |
| `createdFrom` / `createdUntil` | `created_at_start` / `created_at_end`, só a data (`yyyy-MM-dd`) | `range=date_created`, `begin_date` / `end_date` com data e hora |
| Regra das datas | as duas juntas, em ordem, com **no máximo 90 dias** entre elas | as duas juntas, `createdFrom` não depois de `createdUntil` |

Em todos os filtros, o status `UNKNOWN` é recusado.

## 8. `RequestOptions`: idempotência e timeout

```java
RequestOptions options = RequestOptions.builder()
        .idempotencyKey("plano-anual-2026")
        .timeout(Duration.ofSeconds(30))
        .build();
Plan plan = recurrence.plan().create(request, options);
```

| Campo | Efeito |
|---|---|
| `idempotencyKey` | enviada no header `X-Idempotency-Key` (nos dois provedores) |
| `timeout` | substitui o `ProviderConfig.timeout` só nesta chamada |

Aceitam `RequestOptions` as operações que alteram estado: `create`, `update`, `activate`
e `deactivate` de plano; `create`, `update`, `suspend`, `cancel` e `activate` de assinatura;
e `tokenize`. Consultas, listagens e todo o `InvoiceService` não recebem `RequestOptions` e
usam o timeout da configuração.

A biblioteca só envia o header: quem garante a idempotência é o provedor. Alguns detalhes
de cada adaptador:

- **PagBank, `subscription().update` com cartão e plano/valor juntos:** são dois `PUT`
  (`/customers/{id}/billing_info` e `/subscriptions/{id}`) e os dois levam a mesma chave.
- **PagBank, `card().tokenize`:** a criptografia é local; as opções só valem para a consulta
  a `GET /public-keys`, quando a chave não está na configuração.
- **Mercado Pago, `activate`/`deactivate`/`suspend`/`cancel`:** viram um `PUT` do recurso
  com o novo `status`, e a chave vai nesse `PUT`.

## 9. Tratamento de erros

Todas as exceções são **não checadas** e estendem `RecurrenceException`
(pacote `com.recurrence.exception`):

| Exceção | Quando | Dados extras |
|---|---|---|
| `RecurrenceValidationException` | pedido inválido, recusado **antes** da chamada HTTP (campo obrigatório, valor fora da faixa, operação ou filtro sem suporte no provedor ativo) | mensagem, por exemplo `invalid request, planId is required.` |
| `RecurrenceConfigurationException` | configuração inválida, em `ProviderConfig.build()` ou `Recurrence.config(...)` | mensagem com o nome do campo ou da variável |
| `RecurrenceApiException` | o provedor respondeu fora de 2xx | `getStatusCode()`, `getCategory()`, `getErrors()`, `getProvider()`, `getOperation()`, `getRawBody()` |
| `RecurrenceNetworkException` | sem resposta: timeout, falha de IO ou thread interrompida | `getUrl()` |
| `RecurrenceSerializationException` | falha ao converter JSON, data inválida na resposta, chave pública do PagBank inválida | causa original |

`PageOptions.Builder.build()` é a exceção à regra: lança `IllegalArgumentException`.

### 9.1 `RecurrenceApiException`

`getCategory()` é calculado por `ErrorCategory.fromStatus(statusCode)`:

| Status HTTP | `ErrorCategory` |
|---|---|
| 400, 422 | `INVALID_REQUEST` |
| 401, 403 | `AUTHENTICATION` |
| 404 | `NOT_FOUND` |
| 409 | `CONFLICT` |
| 429 | `RATE_LIMITED` |
| 5xx | `PROVIDER_ERROR` |
| outro | `UNKNOWN` |

`getErrors()` devolve uma lista de `ApiError(code, message, field)`, normalizada a partir
do corpo de erro de cada provedor:

| | PagBank | Mercado Pago |
|---|---|---|
| Origem | `error_messages[]` | `cause[]`; sem ele, `code` ou `error` + `message` |
| `code` | `error` | `cause.code`, ou `code`/`error`/status |
| `message` | `description` | `cause.description`, ou `message` |
| `field` | `parameter_name` | sempre `null` |
| Corpo vazio ou ilegível | `ApiError("pagbank_error", "PagBank returned HTTP <status> without details", null)` | `ApiError("mercado_pago_error", "Mercado Pago returned HTTP <status> without details", null)` |

`getOperation()` identifica a chamada: `plan.create`, `plan.get`, `plan.list`, `plan.update`,
`plan.activate`, `plan.deactivate`, `subscription.create`, `subscription.get`,
`subscription.list`, `subscription.update`, `subscription.suspend`, `subscription.cancel`,
`subscription.activate`, `subscription.invoices.list`, `invoice.get`, `invoice.payments.list`,
`payment.get`, `payment.list`, `card.tokenize` (Mercado Pago), `card.public_key` (PagBank) e
`subscription.update.billing_info` (troca de cartão no PagBank). A mensagem da exceção segue
o formato `<PROVEDOR> <operação> failed with HTTP <status>: <primeira mensagem>`.

### 9.2 Exemplo

```java
try {
    recurrence.plan().get("PLAN_INEXISTENTE");
} catch (RecurrenceValidationException e) {
    // pedido inválido, recusado antes de qualquer chamada HTTP
    System.err.println("Corrija o pedido: " + e.getMessage());
} catch (RecurrenceApiException e) {
    // o provedor respondeu com status fora de 2xx
    if (e.getCategory() == ErrorCategory.NOT_FOUND) {
        System.err.println("Plano não existe");
    } else if (e.getCategory() == ErrorCategory.RATE_LIMITED
            || e.getCategory() == ErrorCategory.PROVIDER_ERROR) {
        System.err.println("Tente de novo mais tarde");
    }
    for (ApiError error : e.getErrors()) {
        System.err.println(error.code() + " " + error.message() + " " + error.field());
    }
    System.err.println(e.getProvider() + " " + e.getOperation() + " HTTP " + e.getStatusCode());
} catch (RecurrenceNetworkException e) {
    // sem resposta: timeout, falha de IO
    System.err.println("Falha de rede em " + e.getUrl());
} catch (RecurrenceException e) {
    // demais erros da lib (configuração, serialização)
    System.err.println(e.getMessage());
}
```

O código de tratamento não depende do provedor: a categoria e a lista de `ApiError` têm o
mesmo formato nos dois.

## 10. Valores `UNKNOWN`

`PlanStatus`, `SubscriptionStatus`, `InvoiceStatus`, `PaymentStatus` e `PaymentMethodType`
têm o valor `UNKNOWN`. Ele aparece quando o provedor devolve um estado sem equivalente no
contrato (por exemplo, um status novo ou um pagamento em boleto no PagBank). A leitura não
quebra, e o seu código decide o que fazer:

```java
switch (subscription.getStatus()) {
    case ACTIVE -> System.out.println("em dia");
    case OVERDUE, PENDING -> System.out.println("aguardando pagamento");
    case SUSPENDED, CANCELED, EXPIRED -> System.out.println("sem acesso");
    case UNKNOWN -> System.out.println("estado novo do provedor: tratar com cautela");
}
```

`UNKNOWN` não é aceito em filtros nem em pedidos. Os mapas completos de estado por
provedor estão em [`modelo-canonico.md`](modelo-canonico.md#6-enums-e-mapas-de-estado).
