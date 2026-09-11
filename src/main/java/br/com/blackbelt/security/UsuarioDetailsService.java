package br.com.blackbelt.security;

import br.com.blackbelt.domain.model.Usuario;
import br.com.blackbelt.domain.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identificador)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByUsernameOrEmail(identificador, identificador)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuário não encontrado."
                        ));

        var authorities = usuario.getPerfil().getPermissoes()
                .stream()
                .map(permissao ->
                        new SimpleGrantedAuthority(permissao.getNome()))
                .collect(Collectors.toSet());

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .disabled(!Boolean.TRUE.equals(usuario.getAtivo()))
                .roles(usuario.getPerfil().getNome())
                .authorities(authorities)
                .build();
    }
}