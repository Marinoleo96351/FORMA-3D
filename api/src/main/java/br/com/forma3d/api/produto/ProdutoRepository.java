package br.com.forma3d.api.produto;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

    /** Vitrine publica: so os ativos, na ordem definida pelo dono. */
    List<Produto> findByAtivoTrueOrderByOrdemAscCriadoEmDesc();

    /** Painel: todos, inclusive inativos. */
    List<Produto> findAllByOrderByOrdemAscCriadoEmDesc();
}
