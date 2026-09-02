package br.com.forma3d.api.configuracao;

import br.com.forma3d.api.configuracao.dto.ConfiguracaoRequest;
import br.com.forma3d.api.configuracao.dto.ConfiguracaoResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Edicao da configuracao do site pelo painel. Protegida por token (cai em /api/admin/**). */
@RestController
@RequestMapping("/api/admin/configuracao")
public class AdminConfiguracaoController {

    private final ConfiguracaoSiteService servico;

    public AdminConfiguracaoController(ConfiguracaoSiteService servico) {
        this.servico = servico;
    }

    @PutMapping
    public ConfiguracaoResponse atualizar(@RequestBody @Valid ConfiguracaoRequest req) {
        return ConfiguracaoResponse.de(servico.atualizarFotoTopo(req.fotoTopoUrl()));
    }
}
