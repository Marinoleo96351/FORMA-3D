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

| Variavel              | Padrao                                        | Para que serve                                  |
|-----------------------|-----------------------------------------------|-------------------------------------------------|
| `DB_URL`              | `jdbc:postgresql://localhost:5432/forma3d`    | Banco                                           |
| `DB_USER`             | `forma3d`                                     | Banco                                           |
| `DB_PASSWORD`         | `forma3d`                                     | Banco                                           |
| `CORS_ORIGENS`        | `http://localhost:5173`                       | Enderecos da interface autorizados              |
| `PORT`                | `8080`                                        | Porta do servidor                              |
| `JWT_SEGREDO`         | valor de dev (>= 32 caracteres)              | Assina o token. **Obrigatorio em producao.**    |
| `JWT_EXPIRACAO_HORAS` | `12`                                          | Validade do token                              |
| `ADMIN_EMAIL`         | `admin@forma3d.com.br`                        | Login do painel (criado na 1a inicializacao)    |
| `ADMIN_SENHA`         | `forma3d-troque-em-producao`                  | Senha do painel. **Troque em producao.**        |
| `CLOUDINARY_URL`      | vazio                                         | `cloudinary://chave:segredo@nuvem`. Sem ela, o upload responde 503. |
| `UPLOAD_PASTA`        | `forma3d/produtos`                            | Pasta na Cloudinary onde as fotos ficam        |

O usuario do painel e criado na inicializacao a partir de `ADMIN_EMAIL`/`ADMIN_SENHA`
se ainda nao existir (o hash da senha nunca fica no repositorio). Para trocar a
senha depois, apague a linha da tabela `usuario` e suba de novo com os novos valores.

## Testar

- Testes automatizados: `./mvnw test`
- Rotas HTTP: abra `requests.http` no editor, rode o `### Login` primeiro e depois as demais.

## Rotas

Publicas (sem token):

| Metodo | Rota               | O que faz                                     |
|--------|--------------------|-----------------------------------------------|
| POST   | `/api/auth/login`  | Recebe `email` e `senha`, devolve o token     |
| GET    | `/api/produtos`    | Vitrine: so ativos, ordenados                 |

Protegidas (exigem cabecalho `Authorization: Bearer <token>`):

| Metodo | Rota                              | O que faz                               |
|--------|-----------------------------------|-----------------------------------------|
| GET    | `/api/admin/produtos`             | Todos, inclusive inativos               |
| GET    | `/api/admin/produtos/{id}`        | Um produto                              |
| POST   | `/api/admin/produtos`             | Cria                                    |
| PUT    | `/api/admin/produtos/{id}`        | Atualiza                                |
| DELETE | `/api/admin/produtos/{id}`        | Remove                                  |
| PATCH  | `/api/admin/produtos/{id}/ordem`  | Altera so o campo `ordem`               |
| POST   | `/api/admin/upload`               | Recebe `arquivo` (multipart), devolve `{ "url": "..." }` |

Sem token as rotas protegidas respondem `401`; com token invalido ou expirado, `401` tambem.
Erros saem sempre como `{ "mensagem": "texto em portugues" }`.

## Armazenamento de imagem

As fotos vao para a [Cloudinary](https://cloudinary.com) (plano gratuito). Crie uma
conta, copie a `CLOUDINARY_URL` do painel (`Dashboard` > `API keys`) e exporte como
variavel de ambiente antes de subir a API. A troca de provedor mexe so em
`CloudinaryArmazenamento` (implementa `ArmazenamentoDeImagem`).
