package br.com.forma3d.api.configuracao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Configuracao geral do site. A tabela tem sempre uma linha so (id = 1);
 * o painel edita essa linha, a pagina inicial le dela. Ver instrucao da tarefa 2.
 */
@Entity
@Table(name = "configuracao_site")
public class ConfiguracaoSite {

    /** Id da unica linha da tabela. */
    public static final short ID_UNICO = 1;

    @Id
    private Short id = ID_UNICO;

    @Column(name = "foto_topo_url", length = 500)
    private String fotoTopoUrl;

    @Column(name = "atualizado_em", nullable = false)
    private OffsetDateTime atualizadoEm;

    @PrePersist
    @PreUpdate
    void aoGravar() {
        atualizadoEm = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public Short getId() {
        return id;
    }

    public String getFotoTopoUrl() {
        return fotoTopoUrl;
    }

    public void setFotoTopoUrl(String fotoTopoUrl) {
        this.fotoTopoUrl = fotoTopoUrl;
    }

    public OffsetDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
