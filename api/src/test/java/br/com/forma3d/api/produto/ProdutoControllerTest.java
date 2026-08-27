package br.com.forma3d.api.produto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.forma3d.api.comum.RecursoNaoEncontradoException;
import br.com.forma3d.api.produto.dto.ProdutoRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({ProdutoController.class, AdminProdutoController.class})
class ProdutoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProdutoService servico;

    private Produto exemplo(String nome) {
        Produto p = new Produto();
        p.setId(UUID.randomUUID());
        p.setNome(nome);
        p.setCategoria("Decoracao");
        p.setPreco(new BigDecimal("49.90"));
        p.setOrdem(0);
        p.setAtivo(true);
        return p;
    }

    @Test
    void vitrineListaSomenteOResultadoDoServico() throws Exception {
        when(servico.listarVitrine()).thenReturn(List.of(exemplo("Vaso hexagonal")));

        mvc.perform(get("/api/produtos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nome").value("Vaso hexagonal"));
    }

    @Test
    void criarComNomeVazioRetorna400ComMensagemEmPortugues() throws Exception {
        String corpo = """
            {"nome": "", "categoria": "Decoracao", "preco": 10.00}
            """;

        mvc.perform(post("/api/admin/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpo))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensagem").value("O nome e obrigatorio."));
    }

    @Test
    void criarValidoRetorna201() throws Exception {
        when(servico.criar(any(ProdutoRequest.class))).thenReturn(exemplo("Suporte de fone"));

        String corpo = """
            {"nome": "Suporte de fone", "categoria": "Utilidades", "preco": 39.90}
            """;

        mvc.perform(post("/api/admin/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpo))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nome").value("Suporte de fone"));
    }

    @Test
    void buscarIdInexistenteRetorna404ComMensagem() throws Exception {
        UUID id = UUID.randomUUID();
        when(servico.buscar(eq(id))).thenThrow(new RecursoNaoEncontradoException("Produto nao encontrado."));

        mvc.perform(get("/api/admin/produtos/" + id))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.mensagem").value("Produto nao encontrado."));
    }
}
