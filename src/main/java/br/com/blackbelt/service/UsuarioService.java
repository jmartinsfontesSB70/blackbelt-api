package br.com.blackbelt.service;

import br.com.blackbelt.domain.model.Perfil;
import br.com.blackbelt.domain.model.Usuario;
import br.com.blackbelt.domain.repository.PerfilRepository;
import br.com.blackbelt.domain.repository.UsuarioRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return buscarUsuario(id);
    }

    @Transactional
    public Usuario cadastrar(
            Usuario usuario,
            String password,
            Long perfilId) {

        String email = usuario.getEmail().trim().toLowerCase(Locale.ROOT);
        usuario.setEmail(email);

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new EntidadeConflitoException(
                    "Já existe um usuário cadastrado com este e-mail."
            );
        }

        Perfil perfil = buscarPerfil(perfilId);

        usuario.setPassword(
                passwordEncoder.encode(password)
        );

        usuario.setPerfil(perfil);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(
            Long id,
            Usuario dados,
            String password,
            Long perfilId) {

        String email = dados.getEmail().trim().toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmailAndIdNot(
                email,
                id)) {

            throw new EntidadeConflitoException(
                    "Já existe outro usuário cadastrado com este e-mail."
            );
        }

        Usuario usuario = buscarUsuario(id);

        protegerAdmin(usuario);

        Perfil perfil = buscarPerfil(perfilId);

        usuario.setUsername(dados.getUsername());
        usuario.setEmail(email);
        usuario.setAtivo(dados.getAtivo());
        usuario.setPerfil(perfil);

        if (password != null && !password.isBlank()) {
            usuario.setPassword(
                    passwordEncoder.encode(password)
            );
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * Altera a senha de um usuário através do módulo administrativo.
     *
     * Este método não utiliza protegerAdmin(), pois a senha do usuário
     * admin também pode ser alterada através do módulo administrativo.
     */
    @Transactional
    public void alterarSenha(
            Long id,
            String novaSenha) {

        Usuario usuario = buscarUsuario(id);

        if (passwordEncoder.matches(
                novaSenha,
                usuario.getPassword())) {

            throw new EntidadeConflitoException(
                    "A nova senha deve ser diferente da senha atual."
            );
        }

        usuario.setPassword(
                passwordEncoder.encode(novaSenha)
        );

        usuarioRepository.save(usuario);
    }

    /**
     * Permite que o próprio usuário altere sua senha.
     *
     * A senha atual é obrigatoriamente validada antes da alteração.
     */
    @Transactional
    public void alterarMinhaSenha(
            String username,
            String senhaAtual,
            String novaSenha) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Usuário não encontrado."
                        ));

        if (!passwordEncoder.matches(
                senhaAtual,
                usuario.getPassword())) {

            throw new EntidadeConflitoException(
                    "A senha atual é inválida."
            );
        }

        if (passwordEncoder.matches(
                novaSenha,
                usuario.getPassword())) {

            throw new EntidadeConflitoException(
                    "A nova senha deve ser diferente da senha atual."
            );
        }

        usuario.setPassword(
                passwordEncoder.encode(novaSenha)
        );

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluir(Long id) {

        Usuario usuario = buscarUsuario(id);

        protegerAdmin(usuario);

        usuarioRepository.delete(usuario);
    }

    /**
     * Protege o usuário admin contra alterações estruturais.
     *
     * O admin não pode:
     * - alterar username;
     * - alterar perfil;
     * - alterar status;
     * - ser excluído.
     *
     * A alteração de senha é permitida pelos endpoints específicos
     * de alteração de senha.
     */
    private void protegerAdmin(Usuario usuario) {

        if ("admin".equalsIgnoreCase(usuario.getUsername())) {

            throw new EntidadeConflitoException(
                    "O usuário admin é protegido e não pode ser alterado ou excluído."
            );
        }
    }

    private Usuario buscarUsuario(Long id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Usuário de código " + id +
                                        " não encontrado."
                        ));
    }

    private Perfil buscarPerfil(Long perfilId) {

        return perfilRepository.findById(perfilId)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Perfil de código " + perfilId +
                                        " não encontrado."
                        ));
    }
}
