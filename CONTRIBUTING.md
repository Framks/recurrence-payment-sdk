# Como contribuir

Contribuições são bem-vindas: correções, novos campos no contrato, testes, documentação
ou um novo provedor.

## Fluxo

1. Abra uma issue descrevendo o problema ou a proposta antes de mudanças grandes.
2. Faça um fork, crie uma branch a partir de `master` e envie um pull request.
3. Todo PR passa pelo CI e precisa ser aprovado pelo mantenedor (veja `.github/CODEOWNERS`)
   antes do merge. Isso vale também para os PRs do próprio mantenedor: ninguém faz push
   direto na `master`.

## Ambiente

- JDK 17 ou superior para rodar o Gradle (a *toolchain* do projeto usa Java 16 e é
  baixada automaticamente).
- Build completo: `./gradlew build javadoc`

## Regras

- Leia [`docs/arquitetura.md`](docs/arquitetura.md) e
  [`docs/criterios.md`](docs/criterios.md) antes de mexer no contrato.
- Novo provedor: siga [`docs/novo-provedor.md`](docs/novo-provedor.md).
- Se o comportamento mudar, atualize a documentação em `docs/`.
- **Nunca** coloque tokens, chaves ou dados reais de clientes/cartões no código, em testes
  ou em exemplos. Credenciais de sandbox vêm de variáveis de ambiente
  (`ProviderConfig.fromEnv()`).

## Licença das contribuições

O projeto usa a [Apache License 2.0](LICENSE). Ao enviar um pull request, você concorda
que a sua contribuição é licenciada nos mesmos termos (seção 5 da licença).

## Publicação (mantenedor)

A biblioteca é publicada no Maven Central como `io.github.framks:recurrence-payment-sdk`.

### Configuração única

1. Conta em <https://central.sonatype.com> com login pelo GitHub (o namespace
   `io.github.framks` é verificado automaticamente).
2. *User token*: em **View Account > Generate User Token**. Vai em
   `~/.gradle/gradle.properties`, **nunca** no repositório:

   ```properties
   mavenCentralUsername=<usuário do token>
   mavenCentralPassword=<senha do token>
   ```

3. Chave GPG para assinar os artefatos:

   ```bash
   gpg --full-generate-key                      # RSA 4096, com senha
   gpg --list-secret-keys --keyid-format short  # anote o ID (8 caracteres após rsa4096/)
   gpg --keyserver keys.openpgp.org --send-keys <ID>
   gpg --export-secret-keys <ID> > ~/.gnupg/secring.gpg
   ```

   Em `~/.gradle/gradle.properties`:

   ```properties
   signing.keyId=<ID de 8 caracteres>
   signing.password=<senha da chave>
   signing.secretKeyRingFile=/home/<usuário>/.gnupg/secring.gpg
   ```

   Depois de enviar a chave ao keys.openpgp.org, confirme o e-mail que ele envia; sem isso
   a chave fica sem identidade e o Maven Central pode recusar a assinatura.

4. Para publicar pelo GitHub Actions, cadastre em **Settings > Environments > release**
   (ou em **Secrets and variables > Actions**):

   | Secret | Valor |
   |---|---|
   | `MAVEN_CENTRAL_USERNAME` | usuário do user token |
   | `MAVEN_CENTRAL_PASSWORD` | senha do user token |
   | `SIGNING_KEY` | saída de `gpg --armor --export-secret-keys <ID>` |
   | `SIGNING_KEY_PASSWORD` | senha da chave |

### Publicar uma versão

1. Atualize `VERSION_NAME` em `gradle.properties` e o README.
2. Faça o merge na `master`.
3. Crie e envie a tag: `git tag v0.1.0 && git push origin v0.1.0`. O workflow
   `release.yml` publica no Central.

Para publicar da sua máquina: `./gradlew publishToMavenCentral`. Os artefatos ficam em
**Deployments** no central.sonatype.com; clique em **Publish** para liberar. Para testar
sem enviar nada: `./gradlew publishToMavenLocal`.
