package br.com.forma3d.api.configuracao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfiguracaoSiteService {

    private final ConfiguracaoSiteRepository repositorio;

    public ConfiguracaoSiteService(ConfiguracaoSiteRepository repositorio) {
        this.repositorio = repositorio;
    }

    /** A linha e criada pela migracao V3; o orElseGet e so uma rede de seguranca. */
    @Transactional(readOnly = true)
    public ConfiguracaoSite buscar() {
        return repositorio.findById(ConfiguracaoSite.ID_UNICO).orElseGet(ConfiguracaoSite::new);
    }

    @Transactional
    public ConfiguracaoSite atualizarFotoTopo(String fotoTopoUrl) {
        ConfiguracaoSite config = repositorio.findById(ConfiguracaoSite.ID_UNICO)
            .orElseGet(ConfiguracaoSite::new);
        config.setFotoTopoUrl(vazioParaNulo(fotoTopoUrl));
        return repositorio.save(config);
    }

    private String vazioParaNulo(String valor) {
        if (valor == null) {
            return null;
        }
        String limpo = valor.trim();
        return limpo.isEmpty() ? null : limpo;
    }
}
