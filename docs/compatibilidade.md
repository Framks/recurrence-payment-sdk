# Compatibilidade por provedor

Matriz das 19 operações do contrato nos dois provedores. A coluna **Classe (Q2)** é
**preenchida na fase 4**, com o resultado da avaliação de portabilidade nos *sandboxes*.
As colunas de endpoint e as observações já refletem o código atual.

| # | Operação | PagBank | Mercado Pago | Classe (Q2) | Observação (do código) |
|---|---|---|---|---|---|
| 1 | `plan().create` | `POST /plans` | `POST /preapproval_plan` | | Mercado Pago envia `back_url` da configuração. |
| 2 | `plan().get` | `GET /plans/{id}` | `GET /preapproval_plan/{id}` | | |
| 3 | `plan().list` | `GET /plans` | `GET /preapproval_plan/search` | | Filtro `query`: PagBank `reference_id`, Mercado Pago `q`. |
| 4 | `plan().update` | `PUT /plans/{id}` | `PUT /preapproval_plan/{id}` | | Atualização parcial nos dois. |
| 5 | `plan().activate` | `PUT /plans/{id}/activate` | `PUT /preapproval_plan/{id}` (`status = active`) | | |
| 6 | `plan().deactivate` | `PUT /plans/{id}/inactivate` | `PUT /preapproval_plan/{id}` (`status = cancelled`) | | No Mercado Pago o plano lido volta como `INACTIVE` e pode ser reativado. |
| 7 | `subscription().create` | `POST /subscriptions` | `POST /preapproval` | | PagBank exige `referenceId`, nome, CPF/CNPJ, telefone e CVV; Mercado Pago usa só e-mail e `card_token_id`. |
| 8 | `subscription().get` | `GET /subscriptions/{id}` | `GET /preapproval/{id}` | | Mercado Pago não devolve o nome do assinante e devolve o e-mail vazio. |
| 9 | `subscription().list` | `GET /subscriptions` | `GET /preapproval/search` | | Filtro `OVERDUE`/`EXPIRED` recusado no Mercado Pago. |
| 10 | `subscription().update` | `PUT /subscriptions/{id}` e `PUT /customers/{id}/billing_info` | `PUT /preapproval/{id}` | | **Troca de plano recusada no Mercado Pago** (`RecurrenceValidationException`): o sandbox responde 2xx e mantém o plano antigo. |
| 11 | `subscription().suspend` | `PUT /subscriptions/{id}/suspend` | `PUT /preapproval/{id}` (`status = paused`) | | |
| 12 | `subscription().cancel` | `PUT /subscriptions/{id}/cancel` | `PUT /preapproval/{id}` (`status = cancelled`) | | |
| 13 | `subscription().activate` | `PUT /subscriptions/{id}/activate` | `PUT /preapproval/{id}` (`status = authorized`) | | |
| 14 | `invoice().getInvoice` | `GET /invoices/{id}` | `GET /authorized_payments/{id}` | | Mercado Pago: `occurrence` e `plan` nulos. |
| 15 | `invoice().getPayment` | `GET /payments/{id}` | `GET /v1/payments/{id}` | | Mercado Pago: `invoiceId` nulo. PagBank: valor vem da fatura. |
| 16 | `invoice().listPayments(invoiceId, ...)` | `GET /invoices/{id}/payments` | `GET /authorized_payments/{id}` → `GET /v1/payments/{id}` | | Mercado Pago por composição: zero ou um pagamento, filtro aplicado em memória. |
| 17 | `invoice().listPayments(pageOptions, ...)` | `GET /payments` | `GET /v1/payments/search` | | Mercado Pago traz também pagamentos que não são de assinatura. Filtro `UNPAID` recusado no Mercado Pago; intervalo de datas de no máximo 90 dias no PagBank. |
| 18 | `invoice().listInvoiceBySubscription` | `GET /subscriptions/{id}/invoices` | `GET /authorized_payments/search?preapproval_id={id}` | | |
| 19 | `card().tokenize` | `GET /public-keys` + criptografia local | `POST /v1/card_tokens` | | `holderTaxId` obrigatório só no Mercado Pago. |
