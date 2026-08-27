package br.com.forma3d.api.produto;

import br.com.forma3d.api.produto.dto.ProdutoResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Rota publica da vitrine. Ver secao 5 da especificacao. */
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService servico;

    public ProdutoController(ProdutoService servico) {
        this.servico = servico;
    }

    @GetMapping
    public List<ProdutoResponse> listar() {
        return servico.listarVitrine().stream().map(ProdutoResponse::de).toList();
    }
}
