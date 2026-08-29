package br.com.forma3d.api.seguranca;

/** Lancada quando o e-mail ou a senha do login nao conferem. Vira HTTP 401. */
public class CredenciaisInvalidasException extends RuntimeException {

    public CredenciaisInvalidasException() {
        super("E-mail ou senha incorretos.");
    }
}
