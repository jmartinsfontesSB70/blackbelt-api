package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.PermissaoResponse;
import br.com.blackbelt.api.mapper.PermissaoMapper;
import br.com.blackbelt.service.PermissaoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissoes")
public class PermissaoController {

    private final PermissaoService permissaoService;
    private final PermissaoMapper permissaoMapper;

    public PermissaoController(
            PermissaoService permissaoService,
            PermissaoMapper permissaoMapper) {

        this.permissaoService = permissaoService;
        this.permissaoMapper = permissaoMapper;
    }

    @PreAuthorize("hasAuthority('PERFIL_LISTAR')")
    @GetMapping
    public List<PermissaoResponse> listar() {

        return permissaoService.listar()
                .stream()
                .map(permissaoMapper::toResponse)
                .toList();
    }
}