# Como adicionar um provedor

Este passo a passo usa um provedor fictício chamado **Xpay** (pacote `com.recurrence.xpay`).
O caminho é o mesmo seguido pelo PagBank e pelo Mercado Pago, que servem de modelo.

## Resumo: o que muda e o que é novo

**Duas classes existentes mudam**, com uma linha cada:

| Arquivo existente | Mudança |
|---|---|
| `contract/config/Provider.java` | novo valor `XPAY` no enum |
| `core/ProviderFactory.java` | novo `case XPAY -> new XpayProvider(environment, config);` |

`Provider` faz parte do contrato público, então a mudança é visível ao integrador, mas é
só uma adição: nenhum código existente quebra. O `switch` da `ProviderFactory` é
exaustivo, então esquecer o `case` gera erro de compilação.

**Opcional:** constantes de mensagem em `constants/ErrorMessage.java` (como
`PAGBANK_NOT_FOUND_TOKEN`). Dá para mantê-las no próprio pacote do provedor.

**O resto é adição**, num pacote novo:

```
com/recurrence/xpay/
├── XpayProvider.java                 implements RecurrenceProvider
├── constant/Constant.java            URLs, caminhos, parâmetros, nomes de operação
├── service/
│   ├── XpayPlanService.java          extends BaseService implements PlanService
│   ├── XpaySubscriptionService.java  extends BaseService implements SubscriptionService
│   ├── XpayInvoiceService.java       extends BaseService implements InvoiceService
│   └── XpayCardService.java          extends BaseService implements CardService
├── mapper/
│   ├── CommonsMapper.java            valores, estados, datas, páginas
│   ├── XpayPlanMapper.java
│   ├── XpaySubscriptionMapper.java
│   ├── XpayInvoiceMapper.java
│   ├── XpayCardMapper.java
│   ├── XpayFilterMapper.java         filtros do contrato → parâmetros do Xpay
│   └── XpayErrorMapper.java          implements ProviderErrorTranslator
├── domain/                           DTOs (records) no formato JSON do Xpay
└── validator/                        regras que só valem para o Xpay
```

As variáveis de ambiente não exigem código: `EnvironmentConfigLoader` monta os nomes a
partir do enum, então `RECURRENCE_PROVIDER=XPAY`, `RECURRENCE_XPAY_TOKEN`,
`RECURRENCE_XPAY_ENVIRONMENT`, `RECURRENCE_XPAY_TIMEOUT`, `RECURRENCE_XPAY_PUBLIC_KEY` e
`RECURRENCE_XPAY_BACK_URL` passam a funcionar sozinhas.

## Passo a passo

### 1. Registrar o provedor

```java
// contract/config/Provider.java
public enum Provider {
    PAGBANK,
    MERCADO_PAGO,
    XPAY
}

// core/ProviderFactory.java
return switch (provider){
    case PAGBANK -> new PagBankProvider(environment, config);
    case MERCADO_PAGO -> new MercadoPagoProvider(environment, config);
    case XPAY -> new XpayProvider(environment, config);
};
```

### 2. Criar o `XpayProvider`

Siga `PagBankProvider`: dois construtores (um recebe `HttpClientAdapter`, para testes),
validação da configuração e um único `ProviderContext` compartilhado pelos quatro services.

```java
public class XpayProvider implements RecurrenceProvider {
    private final PlanService planService;
    // ... um campo por service

    public XpayProvider(Environment environment, ProviderConfig config) {
        this(environment, config, new HttpClientAdapter());
    }

    public XpayProvider(Environment environment, ProviderConfig config, HttpClientAdapter httpClient) {
        validateConfig(config);
        ProviderContext context = new ProviderContext(
                Provider.XPAY,
                baseUrl(environment),
                config,
                httpClient,
                new XpayErrorMapper()
        );
        this.planService = new XpayPlanService(context);
        // ... os outros três services
    }

    @Override
    public PlanService planService() {
        return planService;
    }
    // ... subscriptionService(), invoiceService(), cardService()
}
```

Em `validateConfig`, recuse com `RecurrenceConfigurationException` o que não se aplica ao
Xpay: header `Authorization` em `defaultHeaders` (o token já vai como `Bearer`), e
`backUrl` se o Xpay não usar URL de retorno (como faz o PagBank) ou a falta dele se usar
(como faz o Mercado Pago).

### 3. Escrever os services

Cada service estende `BaseService` e implementa só os métodos abstratos da interface (as
variações `default` já existem). O padrão é: validar, converter, chamar `execute`.

```java
public class XpayPlanService extends BaseService implements PlanService {

    public XpayPlanService(ProviderContext context) {
        super(context);
    }

    @Override
    public Plan create(CreatePlanRequest plan, RequestOptions requestOptions) {
        RequestValidator.validate(plan);                    // regras comuns
        return execute(
                "plan.create",
                request(POST, "/plans", requestOptions)
                        .body(toJson("plan.create", XpayPlanMapper.toRequest(plan)))
                        .build(),
                XpayPlan.class,
                XpayPlanMapper::toContract
        );
    }

    @Override
    public Plan get(String planId) {
        RequestValidator.requireId(planId, "planId");
        return execute(
                "plan.get",
                request(GET, "/plans/" + segment(planId), null).build(),
                XpayPlan.class,
                XpayPlanMapper::toContract
        );
    }
    // ...
}
```

`BaseService` já cuida de URL base, headers, `Authorization`, `X-Idempotency-Key`, timeout,
JSON e tradução de erros. Use os mesmos nomes de operação dos outros adaptadores
(`plan.create`, `subscription.cancel`, ...; lista completa em `pagbank/constant/Constant.java`),
porque eles aparecem em `RecurrenceApiException.getOperation()`.

### 4. Escrever os mappers

- **Para o provedor:** `CreatePlanRequest` → `XpayPlanRequest`, etc.
- **Para o contrato:** `XpayPlan` → `Plan`. Monte as entidades pelos builders do contrato.
- **Estados:** todo valor desconhecido vira `UNKNOWN` (veja os `toXxxStatus` em
  `pagbank/mapper/CommonsMapper.java`). Nunca lance exceção ao ler um status novo.
- **Valores:** o contrato usa centavos (`Amount.value`). Se o Xpay usar decimal, converta
  como `mercadopago/mapper/CommonsMapper.java` (`toDecimal`/`toCents`).
- **Filtros (`XpayFilterMapper`):** status canônico sem equivalente no Xpay lança
  `RecurrenceValidationException` (`FILTER_STATUS_UNSUPPORTED`), como em
  `MercadoPagoFilterMapper`. `UNKNOWN` lança `FILTER_STATUS_UNKNOWN`.
- **Páginas:** converta a paginação do Xpay em `PageResponse.PageInfo(offset, limit, total)`.

### 5. Traduzir erros

`XpayErrorMapper implements ProviderErrorTranslator` só faz o *parsing* do corpo de erro
para `List<ApiError>`. Nunca devolva `null`: corpo vazio ou ilegível vira um `ApiError`
genérico (veja `PagBankErrorMapper`). Status, `ErrorCategory` e o lançamento da exceção já
estão em `BaseService`.

### 6. Validar o que é só do Xpay

Regras que só valem para o Xpay (campo obrigatório a mais, limite de datas) ficam em
`xpay/validator/` e são chamadas no service depois do `RequestValidator` comum (veja
`pagbank/validator/SubscriptionValidator.java`).

Se o Xpay **não consegue cumprir** uma operação do contrato, lance
`RecurrenceValidationException` antes da chamada, com uma mensagem clara. É o que o
Mercado Pago faz na troca de plano (`MERCADO_PAGO_PLAN_CHANGE_UNSUPPORTED`). Não devolva
um sucesso falso nem ignore o campo (critério 5 e regra de simplicidade de
[`criterios.md`](criterios.md)).

### 7. Criar os DTOs

Records em `xpay/domain/`, só com os campos que os mappers usam. O `ObjectMapper` comum
(`core.Converter`) converte nomes para `snake_case`, ignora campos desconhecidos e não
envia campos nulos.

Se o Xpay **não** usar `snake_case`, anote os campos com `@JsonProperty`. `BaseService.toJson`
e `fromJson` usam sempre o `ObjectMapper` comum.

### 8. Atualizar a documentação

- [`modelo-canonico.md`](modelo-canonico.md): uma coluna Xpay nas tabelas das seções 4 a 6.
- [`compatibilidade.md`](compatibilidade.md): uma coluna Xpay na matriz.
- [`guia-de-uso.md`](guia-de-uso.md): variáveis de ambiente e diferenças de comportamento.

## Checklist

- [ ] `Provider.XPAY` e `case` na `ProviderFactory`
- [ ] `XpayProvider` com `validateConfig` e `ProviderContext`
- [ ] 4 services estendendo `BaseService`, com os nomes de operação padrão
- [ ] mappers de ida e volta, estados desconhecidos em `UNKNOWN`, valores em centavos
- [ ] `XpayErrorMapper` que nunca devolve `null`
- [ ] validators das regras próprias; recusa explícita do que o Xpay não suporta
- [ ] DTOs em `domain/`
- [ ] documentação atualizada
