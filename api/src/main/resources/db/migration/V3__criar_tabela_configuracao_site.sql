-- Configuracao geral do site, editavel pelo painel sem mexer em codigo.
-- Linha unica: o id fica fixo em 1, garantido pelo check.
create table configuracao_site (
    id            smallint    primary key,
    foto_topo_url varchar(500),
    atualizado_em timestamptz not null default now(),
    constraint configuracao_site_linha_unica check (id = 1)
);

-- A linha ja nasce criada; o painel so faz UPDATE nela.
insert into configuracao_site (id, foto_topo_url) values (1, null);
