package br.com.forma3d.api.produto;

import br.com.forma3d.api.produto.dto.OrdemRequest;
import br.com.forma3d.api.produto.dto.ProdutoRequest;
import br.com.forma3d.api.produto.dto.ProdutoResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rotas de administracao do catalogo. Ver secao 5 da especificacao.
 * A protecao por token entra no dia 3 (branch feat/auth-e-upload).
 */
@RestController
@RequestMapping("/api/admin/produtos")
public class AdminProdutoController {

    private final ProdutoService servico;

    public AdminProdutoController(ProdutoService servico) {
        this.servico = servico;
    }

    @GetMapping
    public List<ProdutoResponse> listar() {
        return servico.listarTodos().stream().map(ProdutoResponse::de).toList();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscar(@PathVariable UUID id) {
        return ProdutoResponse.de(servico.buscar(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse criar(@RequestBody @Valid ProdutoRequest req) {
        return ProdutoResponse.de(servico.criar(req));
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(@PathVariable UUID id, @RequestBody @Valid ProdutoRequest req) {
        return ProdutoResponse.de(servico.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable UUID id) {
        servico.remover(id);
    }

    @PatchMapping("/{id}/ordem")
    public ProdutoResponse alterarOrdem(@PathVariable UUID id, @RequestBody @Valid OrdemRequest req) {
        return ProdutoResponse.de(servico.alterarOrdem(id, req.ordem()));
    }
}
