# Compatibilidade por provedor

Matriz das 19 operações do contrato nos dois provedores. A coluna **Resultado no sandbox**
resume os testes de portabilidade feitos nos *sandboxes* com a versão
`v0.0.1-pre-avaliacao` (3 execuções por provedor, de 27 a 29/10/2026, com o mesmo
resultado em todas).

Resultados: **portável** (o mesmo código e os mesmos dados produzem o resultado esperado);
**portável com condição** (produz o resultado esperado, mas exige um passo extra do
integrador); **falha silenciosa** (retorna sucesso, mas o resultado não é o esperado).

| # | Operação | PagBank | Mercado Pago | Resultado no sandbox | Observação |
|---|---|---|---|---|---|
| 1 | `plan().create` | `POST /plans` | `POST /preapproval_plan` | PagBank e Mercado Pago: portável | Mercado Pago envia `back_url` da configuração. |
| 2 | `plan().get` | `GET /plans/{id}` | `GET /preapproval_plan/{id}` | PagBank e Mercado Pago: portável |  |
| 3 | `plan().list` | `GET /plans` | `GET /preapproval_plan/search` | PagBank e Mercado Pago: portável | Filtro `query`: PagBank `reference_id`, Mercado Pago `q`. |
| 4 | `plan().update` | `PUT /plans/{id}` | `PUT /preapproval_plan/{id}` | PagBank e Mercado Pago: portável | Atualização parcial nos dois. |
| 5 | `plan().activate` | `PUT /plans/{id}/activate` | `PUT /preapproval_plan/{id}` (`status = active`) | PagBank e Mercado Pago: portável |  |
| 6 | `plan().deactivate` | `PUT /plans/{id}/inactivate` | `PUT /preapproval_plan/{id}` (`status = cancelled`) | PagBank e Mercado Pago: portável | No Mercado Pago o plano lido volta como `INACTIVE` e pode ser reativado. |
| 7 | `subscription().create` | `POST /subscriptions` | `POST /preapproval` | PagBank e Mercado Pago: portável | PagBank exige `referenceId`, nome, CPF/CNPJ, telefone e CVV; Mercado Pago usa só e-mail e `card_token_id`. |
| 8 | `subscription().get` | `GET /subscriptions/{id}` | `GET /preapproval/{id}` | PagBank e Mercado Pago: portável | Mercado Pago não devolve o nome do assinante e devolve o e-mail vazio. |
| 9 | `subscription().list` | `GET /subscriptions` | `GET /preapproval/search` | PagBank: portável. Mercado Pago: portável; **filtro `referenceId`: falha silenciosa** | Filtro `OVERDUE`/`EXPIRED` recusado no Mercado Pago. No Mercado Pago o filtro `referenceId` é enviado como `external_reference` em `/preapproval/search` e não restringe o resultado: a busca devolve outras assinaturas, sem erro. |
| 10 | `subscription().update` | `PUT /subscriptions/{id}` e `PUT /customers/{id}/billing_info` | `PUT /preapproval/{id}` | PagBank e Mercado Pago: portável (troca de valor) | **Troca de plano no Mercado Pago**: enviada ao provedor; o sandbox responde 2xx e mantém o plano antigo, e a assinatura devolvida reflete isso. Troca de plano e de meio de pagamento não entraram nos testes de portabilidade. |
| 11 | `subscription().suspend` | `PUT /subscriptions/{id}/suspend` | `PUT /preapproval/{id}` (`status = paused`) | PagBank e Mercado Pago: portável |  |
| 12 | `subscription().cancel` | `PUT /subscriptions/{id}/cancel` | `PUT /preapproval/{id}` (`status = cancelled`) | PagBank e Mercado Pago: portável |  |
| 13 | `subscription().activate` | `PUT /subscriptions/{id}/activate` | `PUT /preapproval/{id}` (`status = authorized`) | PagBank e Mercado Pago: portável |  |
| 14 | `invoice().getInvoice` | `GET /invoices/{id}` | `GET /authorized_payments/{id}` | PagBank e Mercado Pago: portável | Mercado Pago: `occurrence` e `plan` nulos. |
| 15 | `invoice().getPayment` | `GET /payments/{id}` | `GET /v1/payments/{id}` | PagBank e Mercado Pago: portável | Mercado Pago: `invoiceId` nulo. PagBank: valor vem da fatura. |
| 16 | `invoice().listPayments(invoiceId, ...)` | `GET /invoices/{id}/payments` | `GET /authorized_payments/{id}` → `GET /v1/payments/{id}` | PagBank e Mercado Pago: portável | Mercado Pago por composição: zero ou um pagamento, filtro aplicado em memória. |
| 17 | `invoice().listPayments(pageOptions, ...)` | `GET /payments` | `GET /v1/payments/search` | PagBank e Mercado Pago: portável | Mercado Pago traz também pagamentos que não são de assinatura. Filtro `UNPAID` recusado no Mercado Pago; intervalo de datas de no máximo 90 dias no PagBank. Os testes não verificaram se os itens devolvidos são de assinatura. |
| 18 | `invoice().listInvoiceBySubscription` | `GET /subscriptions/{id}/invoices` | `GET /authorized_payments/search?preapproval_id={id}` | PagBank: portável. Mercado Pago: **portável com condição** | No Mercado Pago a fatura é gerada de forma assíncrona; nas 3 execuções a primeira fatura com id apareceu depois de 2 a 6 consultas (10 a 51 s). O integrador precisa consultar de novo até ela aparecer. |
| 19 | `card().tokenize` | `GET /public-keys` + criptografia local | `POST /v1/card_tokens` | PagBank e Mercado Pago: portável | `holderTaxId` obrigatório só no Mercado Pago. |
