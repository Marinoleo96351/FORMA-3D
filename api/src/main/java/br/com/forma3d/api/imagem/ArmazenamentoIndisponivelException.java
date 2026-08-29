package br.com.forma3d.api.imagem;

/**
 * O servico de imagem nao esta configurado ou nao respondeu.
 * Vira HTTP 503 com mensagem legivel.
 */
public class ArmazenamentoIndisponivelException extends RuntimeException {

    public ArmazenamentoIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
