package br.com.forma3d.api.seguranca;

import br.com.forma3d.api.seguranca.dto.LoginRequest;
import br.com.forma3d.api.seguranca.dto.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Rota de login. Ver secao 5 da especificacao. */
@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    private final AuthenticationManager gerenciador;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager gerenciador, TokenService tokenService) {
        this.gerenciador = gerenciador;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest req) {
        String email = req.email().trim().toLowerCase();
        try {
            gerenciador.authenticate(new UsernamePasswordAuthenticationToken(email, req.senha()));
        } catch (AuthenticationException e) {
            throw new CredenciaisInvalidasException();
        }
        return tokenService.gerarPara(email);
    }
}
