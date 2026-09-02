package br.com.forma3d.api.configuracao.dto;

import br.com.forma3d.api.configuracao.ConfiguracaoSite;
import java.time.OffsetDateTime;

public record ConfiguracaoResponse(String fotoTopoUrl, OffsetDateTime atualizadoEm) {

    public static ConfiguracaoResponse de(ConfiguracaoSite c) {
        return new ConfiguracaoResponse(c.getFotoTopoUrl(), c.getAtualizadoEm());
    }
}
