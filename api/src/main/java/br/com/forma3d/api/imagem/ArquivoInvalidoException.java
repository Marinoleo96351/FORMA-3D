package br.com.forma3d.api.imagem;

/** Arquivo ausente, vazio ou em formato nao aceito. Vira HTTP 400. */
public class ArquivoInvalidoException extends RuntimeException {

    public ArquivoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
