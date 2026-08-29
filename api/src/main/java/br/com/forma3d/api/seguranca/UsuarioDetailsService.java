package br.com.forma3d.api.seguranca;

import br.com.forma3d.api.usuario.Usuario;
import br.com.forma3d.api.usuario.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Carrega o usuario unico do painel para o Spring Security conferir a senha. */
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repositorio;

    public UsuarioDetailsService(UsuarioRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        String normalizado = email == null ? "" : email.trim().toLowerCase();
        Usuario usuario = repositorio.findByEmail(normalizado)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado."));
        return User.withUsername(usuario.getEmail())
            .password(usuario.getSenhaHash())
            .authorities("ROLE_ADMIN")
            .build();
    }
}
