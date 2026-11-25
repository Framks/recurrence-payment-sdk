# Documentação

Ordem de leitura sugerida:

1. [`guia-de-uso.md`](guia-de-uso.md): configuração, exemplos de todas as operações,
   paginação, filtros, idempotência e tratamento de erros. Comece por aqui se você vai
   integrar a biblioteca.
2. [`modelo-canonico.md`](modelo-canonico.md): o contrato em detalhe. Cada tipo e campo,
   como ele é convertido de e para o PagBank e o Mercado Pago, mapas de estado e
   limitações conhecidas.
3. [`compatibilidade.md`](compatibilidade.md): as 19 operações lado a lado nos dois
   provedores, com o que cada um aceita ou recusa.
4. [`arquitetura.md`](arquitetura.md): pacotes, fluxo de uma chamada, padrões de projeto
   e decisões de projeto.
5. [`novo-provedor.md`](novo-provedor.md): passo a passo para adicionar um terceiro
   provedor.
6. [`criterios.md`](criterios.md): os critérios que decidem se um campo ou operação entra
   no contrato.
7. [`operacoes.csv`](operacoes.csv): o levantamento das operações dos dois provedores
   (incluindo as que ficaram fora do contrato) e a equivalência de cada uma.

O Javadoc do contrato é gerado com `./gradlew javadoc` em `lib/build/docs/javadoc/`.
