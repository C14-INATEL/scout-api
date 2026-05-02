# Scout API

Scout é uma API REST para um jogo fantasy de futebol, inspirado em competições no estilo Cartola. O backend controla todo o ciclo do jogo: cadastro de presidentes, draft de jogadores, simulação do campeonato, transferências e relatórios finais.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Web
- Spring Data JPA / Hibernate
- Bean Validation
- PostgreSQL 16
- Docker Compose
- Lombok
- SpringDoc OpenAPI / Swagger UI
- Maven Wrapper

## Estrutura do Projeto

O código da aplicação fica dentro da pasta `Scout5`.

- `controller`: endpoints REST.
- `service`: regras de negócio e fluxo do jogo.
- `model`: entidades JPA e enums.
- `repository`: acesso ao banco de dados com Spring Data JPA.
- `dto`: objetos de requisição e resposta.
- `config`: configurações da aplicação e dados iniciais.
- `exception`: tratamento de erros da API.

## Fluxo do Jogo

1. `REGISTRATION`: os presidentes são cadastrados. Cada presidente começa com um orçamento inicial.
2. `DRAFT_AUCTION`: jogadores especiais entram em leilão. Os presidentes fazem lances, e o maior lance ganha o jogador.
3. `DRAFT_LOTTERY`: os jogadores restantes são distribuídos automaticamente para completar os times.
4. `CHAMPIONSHIP`: a tabela do campeonato é gerada e as partidas podem ser simuladas por rodada ou todas de uma vez.
5. `TRANSFER_WINDOW`: durante o campeonato, os presidentes podem trocar jogadores com o mercado ou negociar entre si.
6. `FINISHED`: o campeonato é encerrado e os relatórios finais ficam disponíveis.

## Principais Áreas da API

- `/api/game`: estado do jogo e troca de fases.
- `/api/presidents`: cadastro e consulta de presidentes.
- `/api/players`: catálogo de jogadores e jogadores disponíveis.
- `/api/auction`: leilão atual, lances e finalização de leilão.
- `/api/lottery`: sorteio automático do draft.
- `/api/championship`: geração da tabela e simulação de partidas.
- `/api/transfers`: trocas com o mercado e negociações entre presidentes.
- `/api/reports`: classificação, artilharia, melhor ataque, melhor defesa e relatório completo.

## Como Rodar Localmente

Crie um arquivo `.env` dentro da pasta `Scout5`:

```env
POSTGRES_DB=scout
POSTGRES_USER=scout
POSTGRES_PASSWORD=scout
POSTGRES_PORT=5432
```

Suba o banco de dados:

```bash
cd Scout5
docker compose up -d
```

Rode a API:

```bash
./mvnw spring-boot:run
```

No Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

A API fica disponível em:

```text
http://localhost:8080
```

O Swagger UI fica disponível em:

```text
http://localhost:8080/swagger-ui/index.html
```

## Testes

Rode a suíte de testes com:

```bash
cd Scout5
./mvnw test
```

No Windows PowerShell:

```powershell
.\mvnw.cmd test
```
