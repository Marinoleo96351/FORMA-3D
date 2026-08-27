package br.com.forma3d.api.produto;

import br.com.forma3d.api.comum.RecursoNaoEncontradoException;
import br.com.forma3d.api.produto.dto.ProdutoRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    private final ProdutoRepository repositorio;

    public ProdutoService(ProdutoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<Produto> listarVitrine() {
        return repositorio.findByAtivoTrueOrderByOrdemAscCriadoEmDesc();
    }

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return repositorio.findAllByOrderByOrdemAscCriadoEmDesc();
    }

    @Transactional(readOnly = true)
    public Produto buscar(UUID id) {
        return repositorio.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Produto nao encontrado."));
    }

    @Transactional
    public Produto criar(ProdutoRequest req) {
        Produto p = new Produto();
        aplicar(p, req);
        return repositorio.save(p);
    }

    @Transactional
    public Produto atualizar(UUID id, ProdutoRequest req) {
        Produto p = buscar(id);
        aplicar(p, req);
        return repositorio.save(p);
    }

    @Transactional
    public void remover(UUID id) {
        if (!repositorio.existsById(id)) {
            throw new RecursoNaoEncontradoException("Produto nao encontrado.");
        }
        repositorio.deleteById(id);
    }

    @Transactional
    public Produto alterarOrdem(UUID id, int ordem) {
        Produto p = buscar(id);
        p.setOrdem(ordem);
        return repositorio.save(p);
    }

    private void aplicar(Produto p, ProdutoRequest req) {
        p.setNome(req.nome().trim());
        p.setCategoria(req.categoria().trim());
        p.setPreco(req.preco());
        p.setObservacao(vazioParaNulo(req.observacao()));
        p.setFotoUrl(vazioParaNulo(req.fotoUrl()));
        p.setLinkShopee(vazioParaNulo(req.linkShopee()));
        p.setOrdem(req.ordem() != null ? req.ordem() : 0);
        p.setAtivo(req.ativo() != null ? req.ativo() : true);
    }

    private String vazioParaNulo(String valor) {
        if (valor == null) {
            return null;
        }
        String limpo = valor.trim();
        return limpo.isEmpty() ? null : limpo;
    }
}
