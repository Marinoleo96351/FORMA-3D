-- Usuario unico do painel. Ver secao 4 da especificacao.
-- A linha do administrador nao entra aqui: o hash da senha nao fica versionado.
-- Ela e criada na inicializacao (SeedUsuarioAdmin) a partir de ADMIN_EMAIL/ADMIN_SENHA.
create table usuario (
    id         uuid         primary key default gen_random_uuid(),
    email      varchar(160) not null unique,
    senha_hash varchar(100) not null,
    criado_em  timestamptz  not null default now()
);
