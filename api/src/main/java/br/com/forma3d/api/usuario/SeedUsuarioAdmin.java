package br.com.forma3d.api.usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Garante que o usuario unico do painel exista.
 *
 * O plano permite criar esse usuario "por migracao ou por comando de inicializacao"
 * (secao 4). Fica na inicializacao, e nao no SQL, porque o hash da senha nao deve
 * ser versionado e porque em producao email e senha vem de variavel de ambiente.
 * Se o usuario ja existe, nada e alterado.
 */
@Component
public class SeedUsuarioAdmin implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SeedUsuarioAdmin.class);

    private final UsuarioRepository repositorio;
    private final PasswordEncoder encoder;
    private final String email;
    private final String senha;

    public SeedUsuarioAdmin(
        UsuarioRepository repositorio,
        PasswordEncoder encoder,
        @Value("${app.admin.email}") String email,
        @Value("${app.admin.senha}") String senha
    ) {
        this.repositorio = repositorio;
        this.encoder = encoder;
        this.email = email == null ? "" : email.trim().toLowerCase();
        this.senha = senha;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (email.isBlank() || senha == null || senha.isBlank()) {
            log.warn("ADMIN_EMAIL/ADMIN_SENHA nao definidos; usuario do painel nao foi criado.");
            return;
        }
        if (repositorio.findByEmail(email).isPresent()) {
            log.info("Usuario do painel ja existe ({}).", email);
            return;
        }
        repositorio.save(new Usuario(email, encoder.encode(senha)));
        log.info("Usuario do painel criado ({}).", email);
    }
}
