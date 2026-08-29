package br.com.forma3d.api.imagem;

import org.springframework.web.multipart.MultipartFile;

/**
 * Guarda uma imagem em um servico externo e devolve a URL publica.
 * A implementacao atual usa a Cloudinary; trocar de provedor e trocar so a classe.
 */
public interface ArmazenamentoDeImagem {

    String salvar(MultipartFile arquivo);
}
