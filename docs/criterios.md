# Critérios do Modelo Canonico

Uma operação ou conceito é candidato ao Modelo Canonico quando:

1. possui intenção de negócio equivalente nos provedores analisados;
2. pode ser utilizado sem o consumidor conhecer o fornecedor ativo;
3. permite implementação honesta por todos os adaptadores do núcleo;
4. conserva significado útil diante da inclusão de um terceiro provedor;
5. não exige retornar sucesso fictício quando o recurso está ausente.

## Perguntas obrigatórias

Para cada campo e operação:

- O nome é diferente ou o significado também é diferente?
- O campo participa da criação, atualização, resposta ou de mais de uma delas?
- `null` significa ausência, ilimitado, padrão do provedor ou remoção?
- O valor pode ser transformado sem perda?
- Existe regra dependente de país, conta ou meio de pagamento?
- Como um valor novo ou desconhecido será preservado?
- A biblioteca consegue validar a regra antes da chamada?

## Regra de simplicidade

Uma exceção de operação não suportada é aceitável somente para uma intenção
considerada comum cuja indisponibilidade seja descoberta em execução. Uma
funcionalidade sabidamente exclusiva não deve ser adicionada ao contrato comum.

## Aplicação atual dos critérios — pontos de atenção

Auditoria feita ao revisar `modelo-canonico.md` contra o `contract`
implementado. Nenhum item foi corrigido no código; ficam aqui como achado
para decisão futura (detalhe de cada um na seção 12 de `modelo-canonico.md`).

- **Critério 1 (equivalência entre provedores) — `ProviderConfig.backUrl`**:
  campo específico do fluxo de redirecionamento do Mercado Pago, sem
  contrapartida no PagBank, hoje dentro de um tipo compartilhado. Mitigado
  por validação em tempo de configuração (o PagBank recusa a config se o
  campo vier preenchido), mas o critério pede equivalência de negócio para
  entrar num tipo comum — este campo não tem.
- **Pergunta obrigatória "como um valor desconhecido é preservado?" — enums
  de status**: `PlanStatus`, `SubscriptionStatus`, `PaymentStatus` e
  `InvoiceStatus` não têm valor de fallback (`UNKNOWN` ou equivalente). Um
  estado novo do provedor não tem para onde ir sem quebrar o enum.
- **Critério 4 (conserva significado útil diante de um terceiro provedor) —
  `InvoiceStatus`**: reduzido a `UNPAID`/`PAID`. Ainda não dá para saber se
  essa granularidade é suficiente para o Mercado Pago, porque o adapter dele
  não existe ainda — reavaliar quando `MercadoPagoInvoiceMapper` (ou
  equivalente) for escrito.
- **Pergunta obrigatória "o valor pode ser transformado sem perda?" —
  `Subscription.plan` / `Subscription.customer`**: hoje embutem o objeto
  completo (`Plan`/`Customer`), não um id. Ainda não foi validado se isso é
  necessário nos dois provedores ou se é herança de como o PagBank responde.
- **Regra de simplicidade — `Refund`**: existe no domínio mas não é
  alcançável por nenhum service; tratado como fora do escopo de v1.0 nesta
  revisão, não como funcionalidade exclusiva de um provedor.
