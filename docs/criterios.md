# Critérios do modelo canônico

Uma operação ou conceito é candidato ao modelo canônico quando:

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

## Exceção registrada: `ProviderConfig.backUrl`

`backUrl` é o único campo de um provedor só num tipo compartilhado e, por isso, não
atende ao critério 1. Ele fica no `ProviderConfig` como exceção: o Mercado Pago exige
`back_url` na criação de planos e assinaturas. O risco é controlado na configuração: o
Mercado Pago recusa a configuração sem o campo (`MERCADO_PAGO_BACK_URL_REQUIRED`) e o PagBank recusa
a configuração com o campo preenchido (`PAGBANK_NOT_SUPPORT_BACK_URL`). Nenhum dos dois
ignora o valor em silêncio.
