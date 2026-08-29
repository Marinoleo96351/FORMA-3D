package br.com.forma3d.api.seguranca;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Escreve { "mensagem": "..." } direto na resposta.
 *
 * Os erros de autenticacao acontecem no filtro do Spring Security, antes de
 * chegar no TratadorDeErros, entao a resposta e montada aqui na mao para manter
 * o mesmo formato do resto da API (secao 5 da especificacao).
 */
final class RespostaErroJson {

    private RespostaErroJson() {
    }

    static void escrever(HttpServletResponse resposta, int status, String mensagem) throws IOException {
        resposta.setStatus(status);
        resposta.setContentType("application/json");
        resposta.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String seguro = mensagem.replace("\\", "\\\\").replace("\"", "\\\"");
        resposta.getWriter().write("{\"mensagem\":\"" + seguro + "\"}");
    }
}
