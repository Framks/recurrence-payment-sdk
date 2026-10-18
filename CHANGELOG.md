# Changelog

Formato baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/).

Antes de `0.0.1-pre-avaliacao` não houve versões publicadas. As iterações abaixo foram
reconstruídas a partir do histórico do git e agrupadas por marco; os hashes indicam os
commits principais de cada uma.

## [0.0.1-pre-avaliacao] - 2026-10-18

Versão avaliada no TCC.

### Adicionado
- Documentação reescrita a partir do código: `guia-de-uso.md`, `arquitetura.md`,
  `novo-provedor.md`, `compatibilidade.md` (esqueleto) e `modelo-canonico.md`.
- Javadoc completo das interfaces de serviço, de `Recurrence` e de `ProviderConfig`.
- Linha de tokenização de cartão em `docs/operacoes.csv`.

### Alterado
- Troca de plano no Mercado Pago deixa de ser recusada pelo SDK: o `preapproval_plan_id`
  é enviado e a assinatura devolvida mostra o que o provedor gravou. No sandbox o
  provedor responde 200 e mantém o plano antigo; uma recusa futura chega como
  `RecurrenceApiException`.
- Wrapper do Gradle atualizado de 8.14 para 9.7.1, para a build rodar com o JDK 25.
- `jackson-databind` e `jackson-datatype-jsr310` alinhados na versão 2.17.0.

### Removido
- `Customer` e `Refund` do contrato: nenhum serviço os usava. O assinante aparece só como
  `CustomerReference`; estorno fica fora do escopo.
- Classe de configuração própria do Mercado Pago, que não era mais usada.
- Dependências sem uso: `commons-math3` (exposta como `api`) e `guava`.

## Iteração 6: Mercado Pago e tokenização - 2026-10-08 a 2026-10-12

### Adicionado
- Adaptador do Mercado Pago para planos, assinaturas, faturas e pagamentos (`865e661`, `1ead070`).
- `CardService.tokenize`: criptografia local no PagBank e `POST /v1/card_tokens` no
  Mercado Pago (`18dea49`).
- Configuração por variáveis de ambiente (`RECURRENCE_PROVIDER`,
  `RECURRENCE_<PROVEDOR>_*`) e mista, com o valor do código ganhando (`83a7fcb`).
- Recusa da troca de plano em assinatura no Mercado Pago, porque o sandbox responde 2xx
  e ignora a troca (`865e661`). Revertida em `0.0.1-pre-avaliacao`.

## Iteração 5: contrato para integrador e provedores - 2026-09-08 a 2026-10-06

### Adicionado
- Modelo de erro com `RecurrenceApiException`, `ErrorCategory` e `ApiError`, e o
  pipeline comum `BaseService` com `ProviderContext` e `ProviderErrorTranslator`
  (`ec8fb5b`).
- `RequestValidator` com as regras comuns antes da rede, `QueryParams`, `PlanReference`,
  `CustomerReference` e o valor `UNKNOWN` nos enums de status (`75dce99`).
- Adaptador do PagBank reescrito sobre o novo contrato (`44685d5`).

### Alterado
- Contrato refeito (`a2285db`, `0114974`, `75dce99`): assinatura passa a referenciar
  plano e assinante por `PlanReference`/`CustomerReference`, com um único `BillingInfo` e
  datas como `LocalDate`/`OffsetDateTime`.

### Removido
- `CouponService`, `CustomerService` e o esqueleto do provedor Stripe (`a2285db`).
- Exceções antigas (`RecurrenceHttpException` e afins), substituídas pela nova hierarquia
  (`ec8fb5b`).

## Iteração 4: configuração do provedor e idempotência - 2026-05-29 a 2026-08-23

### Adicionado
- `RequestOptions` com chave de idempotência e timeout (`ad48a40`), aplicado às operações
  que alteram estado (`65e9f5c`).
- `ProviderFactory` (`a58e43e`).

### Alterado
- Criação de provedor reestruturada em torno de `ProviderConfig` (`18b096f`).
- Ajustes de validação (`ad86b14`, `796beaf`).

## Iteração 3: PagBank completo - 2026-03-09 a 2026-04-25

### Adicionado
- Estrutura de pacotes por provedor e `CustomerService` (`f58c8f8`, `28d4511`).
- Assinaturas no PagBank (`5dcf7f8`).
- Faturas, pagamentos, estornos e preferências do PagBank (`6814860`).
- Mapeamento de boleto com PDF e imagem (`8fba757`), removido depois na iteração 5.

## Iteração 2: chamadas HTTP, cupons e paginação - 2026-02-08 a 2026-02-19

### Adicionado
- Métodos de chamada HTTP e `BaseService` inicial (`3e11293`, `a48dcff`).
- Cupons no PagBank (`ada7067`, `21d3af6`).
- Paginação nas respostas (`cee8c2c`).

## Iteração 1: proposta de interface fluente - 2026-02-05 a 2026-02-07

### Adicionado
- Projeto inicial (`c63d67b`).
- Proposta de interface fluente: `Recurrence` com acesso por service (`74449e4`, `9559d98`).

[0.0.1-pre-avaliacao]: https://github.com/Framks/recurrence-payment-sdk/releases/tag/v0.0.1-pre-avaliacao
