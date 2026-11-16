# 💧⚡ AquaVolt — Backend

API REST da **Smart Water & Energy Station**, que monitora o nível de água, a vazão
e o consumo elétrico de uma estação, permite acionar atuadores (bomba, válvula e relé)
e registra falhas e alertas.

Projeto da disciplina **Advanced Programming and Mobile Dev** — Global Solution 2026.
O aplicativo mobile que consome esta API está em: `<link do repositório aquavolt-app>`

## Tecnologias

- Java 17
- Spring Boot 4 (Spring Web, Spring Data JPA)
- H2 Database (modo arquivo, com persistência)
- Lombok
- Maven

## Como executar

**Pré-requisito:** Java 17 instalado.

```bash
git clone <link deste repositório>
cd aquavolt-backend
./mvnw spring-boot:run        # no Windows: mvnw.cmd spring-boot:run
```

A API sobe em `http://localhost:8080`.

## Banco de dados e persistência

Os dados ficam em arquivo (`./data/estacao.mv.db`), então **continuam salvos depois de
reiniciar a aplicação**. O arquivo é criado automaticamente na primeira execução.

Console web do H2: `http://localhost:8080/h2-console`

| Campo | Valor |
|---|---|
| JDBC URL | `jdbc:h2:file:./data/estacao` |
| Usuário | `sa` |
| Senha | *(vazia)* |

## Simulador de leituras

A cada 5 segundos o backend gera leituras simuladas de nível, vazão e consumo elétrico.
Os valores **reagem à bomba**: ligada, o nível sobe e a vazão e o consumo aumentam;
desligada, o nível cai devagar.

Se o nível ficar **abaixo de 20%**, uma falha `CRITICA` é registrada automaticamente.
Ela é resolvida sozinha quando o nível volta a **30% ou mais**.

## Endpoints

### Leituras

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/leituras` | Registra uma leitura |
| GET | `/api/leituras` | Lista o histórico (mais recentes primeiro) |
| GET | `/api/leituras/ultimas` | Última leitura de cada tipo |

Exemplo de corpo (POST):

```json
{ "tipo": "NIVEL_AGUA", "valor": 72.5 }
```

Tipos: `NIVEL_AGUA` (%), `VAZAO` (L/min), `CONSUMO_ELETRICO` (kW).

### Atuadores

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/atuadores` | Cadastra um atuador (começa desligado) |
| GET | `/api/atuadores` | Lista os atuadores e seus estados |
| PUT | `/api/atuadores/{id}/ligar` | Liga o atuador |
| PUT | `/api/atuadores/{id}/desligar` | Desliga o atuador |

Exemplo de corpo (POST):

```json
{ "nome": "Bomba Principal", "tipo": "BOMBA" }
```

Tipos: `BOMBA`, `VALVULA`, `RELE`. Retorna `404` se o `id` não existir.

### Falhas

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/falhas` | Registra uma falha |
| GET | `/api/falhas` | Lista o histórico de falhas |
| GET | `/api/falhas/ativas` | Lista apenas as falhas não resolvidas |
| PUT | `/api/falhas/{id}/resolver` | Marca a falha como resolvida |

Exemplo de corpo (POST):

```json
{ "origem": "Bomba Principal", "descricao": "Superaquecimento do motor", "severidade": "ALTA" }
```

Severidades: `BAIXA`, `MEDIA`, `ALTA`, `CRITICA`.

> Os valores de `tipo` e `severidade` devem ser enviados em **maiúsculas e sem acento**.

## Estrutura do projeto

```
src/main/java/com/aquavolt/aquavolt_backend
├── controller   → endpoints REST
├── model        → entidades JPA e enums
├── repository   → acesso ao banco (Spring Data JPA)
└── service      → simulador de leituras e alertas
```

## Integrantes

| Nome completo          | RM       |
|------------------------|----------|
| `Thiago Almeida Souza` | `565365` |
| `Nicolas Alves`        | `561692` |
| `Gustavo Pereira`      | `<563280>` |