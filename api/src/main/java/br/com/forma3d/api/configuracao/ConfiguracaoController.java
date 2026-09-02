package br.com.forma3d.api.configuracao;

import br.com.forma3d.api.configuracao.dto.ConfiguracaoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Configuracao publica do site: a pagina inicial le a foto do topo daqui. */
@RestController
@RequestMapping("/api/configuracao")
public class ConfiguracaoController {

    private final ConfiguracaoSiteService servico;

    public ConfiguracaoController(ConfiguracaoSiteService servico) {
        this.servico = servico;
    }

    @GetMapping
    public ConfiguracaoResponse obter() {
        return ConfiguracaoResponse.de(servico.buscar());
    }
}
