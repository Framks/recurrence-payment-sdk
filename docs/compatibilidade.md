# Compatibilidade por provedor

Matriz das 19 operações do contrato nos dois provedores. A coluna **Classe (Q2)** traz o
resultado da avaliação de portabilidade nos *sandboxes* (versão `v0.0.1-pre-avaliacao`,
 3 execuções por provedor, de 27 a 29/10/2026, todas com a mesma classe).
Os números entre parênteses são os casos da Q2 que exercitam a operação; o resultado
completo está em `avaliacao_tcc/q2/consolidacao.md`.

Classes: **portável** (atende ao oráculo com o código e os dados comuns); **portável com
condição** (atende, mas exige um passo extra do consumidor); **falha silenciosa** (retorna
sucesso, mas o resultado não atende ao oráculo).

| # | Operação | PagBank | Mercado Pago | Classe (Q2) | Observação (do código) |
|---|---|---|---|---|---|
| 1 | `plan().create` | `POST /plans` | `POST /preapproval_plan` | PagBank e Mercado Pago: portável (1) | Mercado Pago envia `back_url` da configuração. |
| 2 | `plan().get` | `GET /plans/{id}` | `GET /preapproval_plan/{id}` | PagBank e Mercado Pago: portável (2) |  |
| 3 | `plan().list` | `GET /plans` | `GET /preapproval_plan/search` | PagBank e Mercado Pago: portável (3, 4) | Filtro `query`: PagBank `reference_id`, Mercado Pago `q`. |
| 4 | `plan().update` | `PUT /plans/{id}` | `PUT /preapproval_plan/{id}` | PagBank e Mercado Pago: portável (5) | Atualização parcial nos dois. |
| 5 | `plan().activate` | `PUT /plans/{id}/activate` | `PUT /preapproval_plan/{id}` (`status = active`) | PagBank e Mercado Pago: portável (7) |  |
| 6 | `plan().deactivate` | `PUT /plans/{id}/inactivate` | `PUT /preapproval_plan/{id}` (`status = cancelled`) | PagBank e Mercado Pago: portável (6) | No Mercado Pago o plano lido volta como `INACTIVE` e pode ser reativado. |
| 7 | `subscription().create` | `POST /subscriptions` | `POST /preapproval` | PagBank e Mercado Pago: portável (9) | PagBank exige `referenceId`, nome, CPF/CNPJ, telefone e CVV; Mercado Pago usa só e-mail e `card_token_id`. |
| 8 | `subscription().get` | `GET /subscriptions/{id}` | `GET /preapproval/{id}` | PagBank e Mercado Pago: portável (10) | Mercado Pago não devolve o nome do assinante e devolve o e-mail vazio. |
| 9 | `subscription().list` | `GET /subscriptions` | `GET /preapproval/search` | PagBank: portável (11, 12, 13). Mercado Pago: portável (11, 13); **filtro `referenceId`: falha silenciosa (12)** | Filtro `OVERDUE`/`EXPIRED` recusado no Mercado Pago. **Q2:** no Mercado Pago o filtro `referenceId` é enviado como `external_reference` em `/preapproval/search` e não restringe o resultado: a busca devolve outras assinaturas, sem erro (defeito registrado em `avaliacao_tcc/q2/defeitos.md`). |
| 10 | `subscription().update` | `PUT /subscriptions/{id}` e `PUT /customers/{id}/billing_info` | `PUT /preapproval/{id}` | PagBank e Mercado Pago: portável (14, só valor) | **Troca de plano no Mercado Pago**: enviada ao provedor; o sandbox responde 2xx e mantém o plano antigo, e a assinatura devolvida reflete isso. Troca de plano e de meio de pagamento não foram casos da Q2. |
| 11 | `subscription().suspend` | `PUT /subscriptions/{id}/suspend` | `PUT /preapproval/{id}` (`status = paused`) | PagBank e Mercado Pago: portável (15) |  |
| 12 | `subscription().cancel` | `PUT /subscriptions/{id}/cancel` | `PUT /preapproval/{id}` (`status = cancelled`) | PagBank e Mercado Pago: portável (17) |  |
| 13 | `subscription().activate` | `PUT /subscriptions/{id}/activate` | `PUT /preapproval/{id}` (`status = authorized`) | PagBank e Mercado Pago: portável (16) |  |
| 14 | `invoice().getInvoice` | `GET /invoices/{id}` | `GET /authorized_payments/{id}` | PagBank e Mercado Pago: portável (19) | Mercado Pago: `occurrence` e `plan` nulos. |
| 15 | `invoice().getPayment` | `GET /payments/{id}` | `GET /v1/payments/{id}` | PagBank e Mercado Pago: portável (21) | Mercado Pago: `invoiceId` nulo. PagBank: valor vem da fatura. |
| 16 | `invoice().listPayments(invoiceId, ...)` | `GET /invoices/{id}/payments` | `GET /authorized_payments/{id}` → `GET /v1/payments/{id}` | PagBank e Mercado Pago: portável (20) | Mercado Pago por composição: zero ou um pagamento, filtro aplicado em memória. |
| 17 | `invoice().listPayments(pageOptions, ...)` | `GET /payments` | `GET /v1/payments/search` | PagBank e Mercado Pago: portável (22, 23, 24) | Mercado Pago traz também pagamentos que não são de assinatura. Filtro `UNPAID` recusado no Mercado Pago; intervalo de datas de no máximo 90 dias no PagBank. **Q2:** o oráculo não verifica se os itens são de assinatura; a mistura com outros pagamentos não foi medida. |
| 18 | `invoice().listInvoiceBySubscription` | `GET /subscriptions/{id}/invoices` | `GET /authorized_payments/search?preapproval_id={id}` | PagBank: portável. Mercado Pago: **portável com condição** (18) | **Q2:** no Mercado Pago a fatura é gerada de forma assíncrona; nas 3 execuções a primeira fatura com id apareceu depois de 2 a 6 consultas (10 a 51 s). O consumidor precisa consultar de novo até ela aparecer. |
| 19 | `card().tokenize` | `GET /public-keys` + criptografia local | `POST /v1/card_tokens` | PagBank e Mercado Pago: portável (8) | `holderTaxId` obrigatório só no Mercado Pago. |
