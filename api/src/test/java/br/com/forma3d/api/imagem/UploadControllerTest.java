package br.com.forma3d.api.imagem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// Fatia so de controlador: a protecao por token e coberta em AutenticacaoIntegracaoTest.
@WebMvcTest(UploadController.class)
@AutoConfigureMockMvc(addFilters = false)
class UploadControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ArmazenamentoDeImagem armazenamento;

    private MockMultipartFile foto() {
        return new MockMultipartFile("arquivo", "peca.jpg", "image/jpeg", new byte[] {1, 2, 3});
    }

    @Test
    void envioValidoDevolveUrlPublicaEmJson() throws Exception {
        when(armazenamento.salvar(any())).thenReturn("https://forma3d-imagens.r2.dev/abc123.jpg");

        mvc.perform(multipart("/api/admin/upload").file(foto()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.url").value("https://forma3d-imagens.r2.dev/abc123.jpg"));
    }

    @Test
    void formatoInvalidoRetorna400ComMensagem() throws Exception {
        when(armazenamento.salvar(any()))
            .thenThrow(new ArquivoInvalidoException("Formato nao aceito. Envie uma imagem JPG, PNG ou WebP."));

        mvc.perform(multipart("/api/admin/upload").file(foto()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.mensagem").value("Formato nao aceito. Envie uma imagem JPG, PNG ou WebP."));
    }

    @Test
    void armazenamentoIndisponivelRetorna503ComMensagem() throws Exception {
        when(armazenamento.salvar(any()))
            .thenThrow(new ArmazenamentoIndisponivelException("Nao foi possivel enviar a imagem. Tente novamente."));

        mvc.perform(multipart("/api/admin/upload").file(foto()))
            .andExpect(status().isServiceUnavailable())
            .andExpect(jsonPath("$.mensagem").value("Nao foi possivel enviar a imagem. Tente novamente."));
    }
}
