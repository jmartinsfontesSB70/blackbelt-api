package br.com.blackbelt.service;

import br.com.blackbelt.domain.model.RecuperacaoSenha;
import br.com.blackbelt.domain.model.Usuario;
import br.com.blackbelt.domain.repository.RecuperacaoSenhaRepository;
import br.com.blackbelt.domain.repository.UsuarioRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class RecuperacaoSenhaService {

    private final RecuperacaoSenhaRepository recuperacaoSenhaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    private final ResendEmailService resendEmailService;

    public RecuperacaoSenhaService(
            RecuperacaoSenhaRepository recuperacaoSenhaRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            ResendEmailService resendEmailService) {

        this.recuperacaoSenhaRepository = recuperacaoSenhaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.resendEmailService = resendEmailService;
    }

    @Transactional
    public String gerarToken(String identificador, String client) {

        Usuario usuario = usuarioRepository
                .findByUsernameOrEmail(identificador, identificador)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Usuário não encontrado."
                        ));

        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        RecuperacaoSenha recuperacao = new RecuperacaoSenha();

        recuperacao.setToken(token);
        recuperacao.setUsuario(usuario);
        recuperacao.setExpiracao(
                LocalDateTime.now().plusMinutes(30)
        );

        recuperacaoSenhaRepository.save(recuperacao);

        resendEmailService.enviarRecuperacaoSenha(
                usuario.getEmail(),
                usuario.getUsername(),
                token,
                client
        );

        return token;

    }

    @Transactional
    public void redefinirSenha(
            String token,
            String novaSenha) {

        RecuperacaoSenha recuperacao =
                recuperacaoSenhaRepository.findByToken(token)
                        .orElseThrow(() ->
                                new EntidadeNaoEncontradaException(
                                        "Token de recuperação inválido."
                                ));

        if (recuperacao.getUtilizadoEm() != null) {

            throw new EntidadeConflitoException(
                    "O token de recuperação já foi utilizado."
            );
        }

        if (LocalDateTime.now()
                .isAfter(recuperacao.getExpiracao())) {

            throw new EntidadeConflitoException(
                    "O token de recuperação expirou."
            );
        }

        Usuario usuario = recuperacao.getUsuario();

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

        recuperacao.setUtilizadoEm(
                LocalDateTime.now()
        );

        usuarioRepository.save(usuario);
        recuperacaoSenhaRepository.save(recuperacao);
    }
}
