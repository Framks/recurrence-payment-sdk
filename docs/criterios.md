# Critérios do modelo canônico

Uma operação ou conceito é candidato ao modelo canônico quando:

1. possui intenção de negócio equivalente nos provedores;
2. pode ser usado sem que a aplicação conheça o provedor ativo;
3. pode ser implementado de forma honesta por todos os adaptadores;
4. mantém significado útil diante de um terceiro provedor
5. exige não retornar sucesso fictício quando o recurso não existe.

## Exceção registrada: `ProviderConfig.backUrl`

`backUrl` é o único campo de um provedor só num tipo compartilhado e, por isso, não
atende ao critério 1. Ele fica no `ProviderConfig` como exceção: o Mercado Pago exige
`back_url` na criação de planos e assinaturas. O risco é controlado na configuração: o
Mercado Pago recusa a configuração sem o campo (`MERCADO_PAGO_BACK_URL_REQUIRED`) e o PagBank recusa
a configuração com o campo preenchido (`PAGBANK_NOT_SUPPORT_BACK_URL`). Nenhum dos dois
ignora o valor em silêncio.
