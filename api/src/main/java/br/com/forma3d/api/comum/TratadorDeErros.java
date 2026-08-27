package br.com.forma3d.api.comum;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Todo erro sai como { "mensagem": "..." } em portugues.
 * A excecao crua do Java nunca chega na interface (secao 5 da especificacao).
 */
@RestControllerAdvice
public class TratadorDeErros {

    private static final Logger log = LoggerFactory.getLogger(TratadorDeErros.class);

    public record ErroResposta(String mensagem) {}

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta naoEncontrado(RecursoNaoEncontradoException e) {
        return new ErroResposta(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta corpoInvalido(MethodArgumentNotValidException e) {
        String detalhe = e.getBindingResult().getFieldErrors().stream()
            .map(erro -> erro.getDefaultMessage())
            .distinct()
            .collect(Collectors.joining(" "));
        return new ErroResposta(detalhe.isBlank() ? "Dados invalidos." : detalhe);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta parametroInvalido(HandlerMethodValidationException e) {
        return new ErroResposta("Dados invalidos na requisicao.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta jsonIlegivel(HttpMessageNotReadableException e) {
        return new ErroResposta("Corpo da requisicao ausente ou mal formado.");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta rotaInexistente(NoResourceFoundException e) {
        return new ErroResposta("Rota nao encontrada.");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta erroInterno(Exception e) {
        log.error("Erro nao tratado", e);
        return new ErroResposta("Nao foi possivel concluir. Tente de novo.");
    }
}
