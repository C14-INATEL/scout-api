# Processo de Desenvolvimento

Este documento define o processo de desenvolvimento adotado pelo grupo no projeto Scout API, incluindo padrão de branches, padrão de commits, fluxo de Pull Requests, regras de revisão e relação com issues.

---

## Padrão de Branches

As alterações do projeto devem ser feitas em branches separadas, de acordo com o tipo de tarefa realizada.

### Tipos de branches

- `feature/nome-da-funcionalidade`  
  Para desenvolvimento de novas funcionalidades.

- `fix/nome-da-correcao`  
  Para correção de bugs ou problemas encontrados.

- `test/nome-do-teste-ou-service`  
  Para criação ou melhoria de testes automatizados.

- `docs/nome-da-documentacao`  
  Para alterações de documentação.

- `ci/nome-da-configuracao`  
  Para configurações de CI/CD.

- `refactor/nome-do-refactor`  
  Para melhorias internas no código sem alterar diretamente o comportamento do sistema.

### Exemplos de branches

```text
feature/create-president
feature/player-auction
fix/president-validation
test/auction-service
test/transfer-service
docs/user-stories
docs/development-process
ci/pipeline-config
refactor/exception-handling
```

---

## Padrão de Commits

Os commits do projeto devem ser escritos em inglês, seguindo um padrão simples baseado no tipo de alteração realizada.

Mesmo que a documentação do projeto esteja em português, os nomes das branches e as mensagens de commit devem ser mantidos em inglês para preservar o padrão do histórico do repositório.

### Tipos de commits

- `feat:` para novas funcionalidades.
- `fix:` para correção de bugs.
- `docs:` para documentação.
- `test:` para criação ou melhoria de testes.
- `refactor:` para refatorações.
- `ci:` para alterações na pipeline de CI/CD.
- `chore:` para tarefas auxiliares que não alteram diretamente o comportamento do sistema.

### Exemplos de commits

```text
docs: add development process standards
docs: add user stories
docs: add traceability table
test: add president service tests
test: add auction service tests
test: add transfer service tests
refactor: improve exception handling
ci: add pipeline configuration
fix: adjust president validation
feat: add player auction validation
```

---

## Regra de Pull Request Obrigatório

Toda alteração relevante deve ser feita em uma branch separada e enviada por Pull Request antes de entrar na branch principal.

Não devem ser feitos commits diretamente na branch `main`, exceto em casos emergenciais e justificados pelo grupo.

Cada Pull Request deve conter:

- Descrição clara do que foi alterado.
- Issue relacionada, quando existir.
- Evidência de teste ou validação, quando aplicável.
- Indicação do tipo de alteração: documentação, teste, código, CI/CD ou refactor.
- Revisão de pelo menos um outro membro do grupo.

### Modelo de descrição de Pull Request

```md
## O que foi feito

Descrever brevemente as alterações realizadas.

## Issue relacionada

Closes #NÚMERO_DA_ISSUE

## Como testar

Explicar como validar a alteração.
```

---

## Regra de Revisão de Pull Requests

O grupo adotou um fluxo de revisão em ciclo para garantir que todos os membros participem ativamente do processo de Pull Request e que nenhum integrante revise o próprio trabalho.

### Ciclo de revisão

- Pull Requests abertos por **Eduardo** devem ser revisados por **Bruno**.
- Pull Requests abertos por **Bruno** devem ser revisados por **Thulio**.
- Pull Requests abertos por **Thulio** devem ser revisados por **Eduardo**.

### Regra geral

Todo Pull Request deve ser revisado por pelo menos um membro que não seja o autor da alteração.

O objetivo desse ciclo é distribuir a responsabilidade de revisão entre todos os integrantes, evitando que apenas uma pessoa fique responsável por aprovar os PRs do grupo.

### Revisões adicionais

Quando o Pull Request envolver alterações mais críticas, como regras de negócio, refactorings importantes, testes principais ou CI/CD, o grupo poderá solicitar uma segunda revisão.

Exemplos:

- Um PR de código crítico aberto por Thulio será revisado por Eduardo e poderá receber uma segunda revisão de Bruno.
- Um PR de testes aberto por Bruno será revisado por Thulio e poderá receber uma segunda revisão de Eduardo.
- Um PR de documentação aberto por Eduardo será revisado por Bruno e poderá receber uma segunda revisão de Thulio se envolver informações técnicas.

### Pontos avaliados no review

Durante a revisão, o revisor deve verificar:

- Se a alteração está de acordo com a issue relacionada.
- Se o padrão de branch foi seguido.
- Se o padrão de commit foi seguido.
- Se o Pull Request possui uma descrição clara.
- Se os testes foram criados ou atualizados, quando necessário.
- Se a documentação foi atualizada, quando aplicável.
- Se não há alterações fora do escopo do PR.
- Se a alteração contribui de forma relevante para o projeto.

### Possíveis decisões no review

- `Approve`: quando o PR está adequado para merge.
- `Request changes`: quando há algo que precisa ser corrigido antes do merge.
- `Comment`: quando há apenas uma sugestão ou dúvida, sem bloquear o merge.

---

## Papéis no Grupo

O grupo adotou uma divisão de papéis para organizar melhor o desenvolvimento e facilitar o acompanhamento das tarefas.

### Eduardo — PO / Facilitador Kanban / Desenvolvedor de apoio

Responsável por:

- Organização do Trello.
- Criação e acompanhamento das issues.
- Histórias de usuário.
- README e documentação.
- Rastreabilidade entre histórias, issues, PRs e testes.
- Documentação do processo de desenvolvimento.
- Documentação do uso de IA.
- Apoio em testes e ajustes de código.

### Thulio — Tech Lead / Backend Core

Responsável por:

- Desenvolvimento das partes principais do backend.
- Revisão de regras de negócio.
- Refactorings.
- Correções técnicas.
- Configuração principal de CI/CD.
- Revisão técnica de PRs de código.
- Apoio na resolução de bugs críticos.

### Bruno — QA / Backend auxiliar / Testes

Responsável por:

- Criação de testes automatizados.
- Validação manual dos endpoints.
- Documentação de evidências técnicas.
- Exemplos de requisições.
- Apoio na pipeline de CI/CD.
- Revisão de PRs relacionados a testes e QA.

---

## Relação com Issues

Sempre que possível, cada Pull Request deve estar relacionado a uma issue do GitHub.

Exemplo:

```text
Closes #3
```

Isso permite rastrear o caminho entre tarefa, desenvolvimento e entrega final.

No caso das histórias de usuário, a rastreabilidade deve seguir o formato:

```text
História de usuário → Issue → Pull Request → Teste automatizado
```

Exemplo:

```text
HU02 - Leilão de jogadores → Issue #2 → PR #8 → AuctionServiceTest
```

---

## Definição de Pronto

Uma tarefa só deve ser considerada concluída quando:

- A alteração foi implementada.
- O commit segue o padrão definido.
- O Pull Request foi aberto.
- O Pull Request foi revisado por outro membro.
- Os testes foram executados, quando aplicável.
- A documentação foi atualizada, quando necessário.
- A issue relacionada foi fechada ou atualizada.
- A alteração foi integrada à branch principal.