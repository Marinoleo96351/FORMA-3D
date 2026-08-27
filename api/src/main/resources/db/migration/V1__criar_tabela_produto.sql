-- Tabela de produtos do catalogo. Ver secao 4 da especificacao.
create table produto (
    id          uuid          primary key default gen_random_uuid(),
    nome        varchar(80)   not null,
    categoria   varchar(60)   not null,
    preco       numeric(10, 2) not null check (preco > 0),
    observacao  varchar(120),
    foto_url    varchar(500),
    link_shopee varchar(500),
    ordem       integer       not null default 0,
    ativo       boolean       not null default true,
    criado_em   timestamptz   not null default now()
);

-- A vitrine publica sempre busca ativos ordenados por ordem, depois por data.
create index idx_produto_vitrine
    on produto (ordem asc, criado_em desc)
    where ativo;
