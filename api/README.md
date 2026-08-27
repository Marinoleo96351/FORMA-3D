# forma 3d — API

Servidor de produtos do catalogo. Java 21 + Spring Boot 4 + PostgreSQL.

## Requisitos

- JDK 21 (`JAVA_HOME` apontando para ele)
- PostgreSQL rodando localmente

## Banco local

Crie o banco e o usuario uma vez (via `psql` com um superusuario):

```sql
CREATE ROLE forma3d LOGIN PASSWORD 'forma3d';
CREATE DATABASE forma3d OWNER forma3d;
```

O Flyway cria a tabela `produto` sozinho na primeira execucao.

## Rodar

```bash
./mvnw spring-boot:run
```

Sobe em `http://localhost:8080`. Variaveis de ambiente aceitas (com os padroes locais):

| Variavel        | Padrao                                        |
|-----------------|-----------------------------------------------|
| `DB_URL`        | `jdbc:postgresql://localhost:5432/forma3d`    |
| `DB_USER`       | `forma3d`                                     |
| `DB_PASSWORD`   | `forma3d`                                     |
| `CORS_ORIGENS`  | `http://localhost:5173`                       |
| `PORT`          | `8080`                                        |

## Testar

- Testes automatizados: `./mvnw test`
- Rotas HTTP: abra `requests.http` no editor e dispare as chamadas na ordem.

## Rotas (dia 2 — ainda sem token)

| Metodo | Rota                              | O que faz                               |
|--------|-----------------------------------|-----------------------------------------|
| GET    | `/api/produtos`                   | Vitrine: so ativos, ordenados           |
| GET    | `/api/admin/produtos`             | Todos, inclusive inativos               |
| GET    | `/api/admin/produtos/{id}`        | Um produto                              |
| POST   | `/api/admin/produtos`             | Cria                                    |
| PUT    | `/api/admin/produtos/{id}`        | Atualiza                                |
| DELETE | `/api/admin/produtos/{id}`        | Remove                                  |
| PATCH  | `/api/admin/produtos/{id}/ordem`  | Altera so o campo `ordem`               |

Erros saem sempre como `{ "mensagem": "texto em portugues" }`.
