package br.com.forma3d.api.produto.dto;

import br.com.forma3d.api.produto.Produto;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ProdutoResponse(
    UUID id,
    String nome,
    String categoria,
    BigDecimal preco,
    String observacao,
    String fotoUrl,
    String linkShopee,
    int ordem,
    boolean ativo,
    OffsetDateTime criadoEm
) {

    public static ProdutoResponse de(Produto p) {
        return new ProdutoResponse(
            p.getId(),
            p.getNome(),
            p.getCategoria(),
            p.getPreco(),
            p.getObservacao(),
            p.getFotoUrl(),
            p.getLinkShopee(),
            p.getOrdem(),
            p.isAtivo(),
            p.getCriadoEm()
        );
    }
}
