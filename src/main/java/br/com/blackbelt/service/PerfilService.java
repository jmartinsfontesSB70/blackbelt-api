package br.com.blackbelt.service;

import br.com.blackbelt.domain.model.Perfil;
import br.com.blackbelt.domain.model.Permissao;
import br.com.blackbelt.domain.repository.PerfilRepository;
import br.com.blackbelt.domain.repository.PermissaoRepository;
import br.com.blackbelt.domain.repository.UsuarioRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final PermissaoRepository permissaoRepository;
    private final UsuarioRepository usuarioRepository;

    public PerfilService(
            PerfilRepository perfilRepository,
            PermissaoRepository permissaoRepository,
            UsuarioRepository usuarioRepository) {

        this.perfilRepository = perfilRepository;
        this.permissaoRepository = permissaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Perfil> listar() {
        return perfilRepository.findAll();
    }

    public Perfil buscarPorId(Long id) {

        return buscarPerfil(id);
    }

    @Transactional
    public Perfil cadastrar(
            Perfil perfil,
            Set<Long> permissaoIds) {

        Set<Permissao> permissoes =
                permissaoRepository.findAllById(permissaoIds)
                        .stream()
                        .collect(Collectors.toSet());

        if (permissoes.size() != permissaoIds.size()) {

            throw new EntidadeNaoEncontradaException(
                    "Uma ou mais permissões não foram encontradas."
            );
        }

        perfil.setPermissoes(permissoes);

        return perfilRepository.save(perfil);
    }

    @Transactional
    public Perfil atualizar(
            Long id,
            Perfil dados,
            Set<Long> permissaoIds) {

        Perfil perfil = buscarPerfil(id);

        protegerPerfilAdmin(perfil);

        Set<Permissao> permissoes =
                permissaoRepository.findAllById(permissaoIds)
                        .stream()
                        .collect(Collectors.toSet());

        if (permissoes.size() != permissaoIds.size()) {

            throw new EntidadeNaoEncontradaException(
                    "Uma ou mais permissões não foram encontradas."
            );
        }

        perfil.setNome(dados.getNome());
        perfil.setPermissoes(permissoes);

        return perfilRepository.save(perfil);
    }

    @Transactional
    public void excluir(Long id) {

        Perfil perfil = buscarPerfil(id);

        protegerPerfilAdmin(perfil);

        if (usuarioRepository.existsByPerfilId(id)) {

            throw new EntidadeConflitoException(
                    "Não é possível excluir o perfil porque existem usuários associados a ele."
            );
        }

        perfilRepository.delete(perfil);
    }

    private void protegerPerfilAdmin(Perfil perfil) {

        if ("ADMIN".equalsIgnoreCase(perfil.getNome())) {

            throw new EntidadeConflitoException(
                    "O perfil ADMIN é protegido e não pode ser alterado ou excluído."
            );
        }
    }

    private Perfil buscarPerfil(Long id) {

        return perfilRepository.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Perfil de código " + id + " não encontrado."
                        ));
    }
}