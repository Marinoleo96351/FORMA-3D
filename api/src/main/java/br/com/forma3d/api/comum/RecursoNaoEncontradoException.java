package br.com.forma3d.api.comum;

/** Lancada quando um id pedido nao existe. Vira HTTP 404 com mensagem legivel. */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
