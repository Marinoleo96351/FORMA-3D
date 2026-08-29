package br.com.forma3d.api.seguranca;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Checkpoint do dia 3: login devolve token, rota protegida recusa sem token
 * e aceita com token, e a vitrine publica continua aberta.
 */
@SpringBootTest(properties = {
    "app.admin.email=teste@forma3d.com.br",
    "app.admin.senha=segredo-de-teste-123"
})
@AutoConfigureMockMvc
class AutenticacaoIntegracaoTest {

    private static final String EMAIL = "teste@forma3d.com.br";
    private static final String SENHA = "segredo-de-teste-123";

    @Autowired
    private MockMvc mvc;

    private String corpoLogin(String email, String senha) {
        return "{\"email\":\"" + email + "\",\"senha\":\"" + senha + "\"}";
    }

    private String obterToken() throws Exception {
        String resposta = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpoLogin(EMAIL, SENHA)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return JsonPath.read(resposta, "$.token");
    }

    @Test
    void loginComCredenciaisCertasDevolveToken() throws Exception {
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpoLogin(EMAIL, SENHA)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty())
            .andExpect(jsonPath("$.tipo").value("Bearer"))
            .andExpect(jsonPath("$.expiraEmSegundos").isNumber());
    }

    @Test
    void loginComSenhaErradaDevolve401ComMensagemLegivel() throws Exception {
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpoLogin(EMAIL, "senha-errada")))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.mensagem").value("E-mail ou senha incorretos."));
    }

    @Test
    void loginComEmailInexistenteDevolve401() throws Exception {
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(corpoLogin("ninguem@forma3d.com.br", SENHA)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.mensagem").value("E-mail ou senha incorretos."));
    }

    @Test
    void loginSemCorpoDevolve400() throws Exception {
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"\",\"senha\":\"\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensagem").isNotEmpty());
    }

    @Test
    void rotaAdminSemTokenDevolve401ComMensagem() throws Exception {
        mvc.perform(get("/api/admin/produtos"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.mensagem").value("Faca login para acessar essa area."));
    }

    @Test
    void rotaAdminComTokenInvalidoDevolve401() throws Exception {
        mvc.perform(get("/api/admin/produtos")
                .header("Authorization", "Bearer nao.e.um.token"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void rotaAdminComTokenValidoDevolve200() throws Exception {
        String token = obterToken();

        mvc.perform(get("/api/admin/produtos")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    void rotaDeUploadTambemExigeToken() throws Exception {
        mvc.perform(post("/api/admin/upload"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void vitrinePublicaContinuaSemToken() throws Exception {
        mvc.perform(get("/api/produtos"))
            .andExpect(status().isOk());
    }
}
