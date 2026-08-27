package br.com.forma3d.api.produto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Percorre todas as rotas de produto do dia 2 contra o Postgres local.
 * Cada teste roda em transacao e faz rollback ao final.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProdutoRotasIntegracaoTest {

    @Autowired
    private MockMvc mvc;

    private String corpo(String nome, String categoria, String preco, Integer ordem, Boolean ativo) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"nome\":\"").append(nome).append("\",");
        sb.append("\"categoria\":\"").append(categoria).append("\",");
        sb.append("\"preco\":").append(preco);
        if (ordem != null) {
            sb.append(",\"ordem\":").append(ordem);
        }
        if (ativo != null) {
            sb.append(",\"ativo\":").append(ativo);
        }
        return sb.append("}").toString();
    }

    private String criar(String nome, String categoria, String preco, Integer ordem, Boolean ativo) throws Exception {
        String resposta = mvc.perform(post("/api/admin/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpo(nome, categoria, preco, ordem, ativo)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return JsonPath.read(resposta, "$.id");
    }

    @Test
    void vitrinePublicaMostraSoAtivosNaOrdemCerta() throws Exception {
        criar("Vaso A", "Decoracao", "30.00", 2, true);
        criar("Vaso B", "Decoracao", "40.00", 1, true);
        criar("Rascunho", "Decoracao", "10.00", 0, false);

        mvc.perform(get("/api/produtos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].nome").value("Vaso B"))
            .andExpect(jsonPath("$[1].nome").value("Vaso A"));
    }

    @Test
    void painelListaTambemInativos() throws Exception {
        criar("Ativo", "Utilidades", "25.00", 0, true);
        criar("Inativo", "Utilidades", "25.00", 0, false);

        mvc.perform(get("/api/admin/produtos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void cicloDeVidaCompletoDeUmProduto() throws Exception {
        String id = criar("Suporte", "Utilidades", "39.90", 1, true);

        mvc.perform(get("/api/admin/produtos/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Suporte"))
            .andExpect(jsonPath("$.criadoEm").isNotEmpty());

        mvc.perform(put("/api/admin/produtos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpo("Suporte grande", "Utilidades", "49.90", 1, true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Suporte grande"));

        mvc.perform(patch("/api/admin/produtos/" + id + "/ordem")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"ordem\": 7}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ordem").value(7));

        mvc.perform(delete("/api/admin/produtos/" + id))
            .andExpect(status().isNoContent());

        mvc.perform(get("/api/admin/produtos/" + id))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.mensagem").value("Produto nao encontrado."));
    }

    @Test
    void nomeVazioRetorna400ComMensagemLegivel() throws Exception {
        mvc.perform(post("/api/admin/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpo("", "Decoracao", "10.00", null, null)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensagem").value("O nome e obrigatorio."));
    }

    @Test
    void precoZeroRetorna400() throws Exception {
        mvc.perform(post("/api/admin/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpo("Teste", "Decoracao", "0", null, null)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensagem").value("O preco deve ser maior que zero."));
    }

    @Test
    void jsonQuebradoRetorna400ComMensagem() throws Exception {
        mvc.perform(post("/api/admin/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ nao e json "))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensagem").value("Corpo da requisicao ausente ou mal formado."));
    }

    @Test
    void idInexistenteRetorna404() throws Exception {
        mvc.perform(get("/api/admin/produtos/00000000-0000-0000-0000-000000000000"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.mensagem").value("Produto nao encontrado."));
    }
}
