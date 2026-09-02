package br.com.forma3d.api.configuracao;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Rotas da configuracao do site: leitura publica e escrita so com token.
 * Cada teste roda em transacao e faz rollback ao final.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ConfiguracaoRotasIntegracaoTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void configuracaoPublicaAbreSemToken() throws Exception {
        mvc.perform(get("/api/configuracao"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists());
    }

    @Test
    void atualizarSemTokenRecusaCom401() throws Exception {
        mvc.perform(put("/api/admin/configuracao")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fotoTopoUrl\":\"https://exemplo.test/topo.jpg\"}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void atualizarComTokenGravaEApareceNaRotaPublica() throws Exception {
        mvc.perform(put("/api/admin/configuracao")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fotoTopoUrl\":\"https://exemplo.test/topo.jpg\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fotoTopoUrl").value("https://exemplo.test/topo.jpg"));

        mvc.perform(get("/api/configuracao"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fotoTopoUrl").value("https://exemplo.test/topo.jpg"));
    }

    @Test
    @WithMockUser
    void fotoTopoVaziaLimpaOValor() throws Exception {
        mvc.perform(put("/api/admin/configuracao")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fotoTopoUrl\":\"https://exemplo.test/topo.jpg\"}"))
            .andExpect(status().isOk());

        mvc.perform(put("/api/admin/configuracao")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fotoTopoUrl\":\"  \"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fotoTopoUrl").value(nullValue()));
    }
}
