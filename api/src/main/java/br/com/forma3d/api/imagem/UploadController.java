package br.com.forma3d.api.imagem;

import br.com.forma3d.api.imagem.dto.UploadResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * POST /api/admin/upload — recebe a foto do produto via multipart/form-data (campo "arquivo")
 * e devolve { "url": "..." } com o endereco publico para o painel gravar em fotoUrl.
 * Rota protegida por token, junto com o resto de /api/admin. Ver secao 5 da especificacao.
 */
@RestController
@RequestMapping("/api/admin/upload")
public class UploadController {

    private final ArmazenamentoDeImagem armazenamento;

    public UploadController(ArmazenamentoDeImagem armazenamento) {
        this.armazenamento = armazenamento;
    }

    @PostMapping
    public UploadResponse enviar(@RequestParam("arquivo") MultipartFile arquivo) {
        return new UploadResponse(armazenamento.salvar(arquivo));
    }
}
