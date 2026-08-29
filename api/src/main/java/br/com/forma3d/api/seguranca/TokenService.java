package br.com.forma3d.api.seguranca;

import br.com.forma3d.api.seguranca.dto.TokenResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/** Emite o token assinado que o painel usa para chamar as rotas protegidas. */
@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final String emissor;
    private final long expiracaoHoras;

    public TokenService(
        JwtEncoder encoder,
        @Value("${app.jwt.emissor}") String emissor,
        @Value("${app.jwt.expiracao-horas}") long expiracaoHoras
    ) {
        this.encoder = encoder;
        this.emissor = emissor;
        this.expiracaoHoras = expiracaoHoras;
    }

    public TokenResponse gerarPara(String email) {
        Instant agora = Instant.now();
        Instant expiraEm = agora.plus(expiracaoHoras, ChronoUnit.HOURS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(emissor)
            .issuedAt(agora)
            .expiresAt(expiraEm)
            .subject(email)
            .build();

        JwsHeader cabecalho = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = encoder.encode(JwtEncoderParameters.from(cabecalho, claims)).getTokenValue();

        return new TokenResponse(token, "Bearer", expiracaoHoras * 3600);
    }
}
