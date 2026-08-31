package br.com.blackbelt.service;

import br.com.blackbelt.domain.model.Permissao;
import br.com.blackbelt.domain.repository.PermissaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;

    public PermissaoService(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    public List<Permissao> listar() {

        return permissaoRepository.findAll();
    }
}