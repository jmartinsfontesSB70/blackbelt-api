package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.PerfilRequest;
import br.com.blackbelt.api.dto.PerfilResponse;
import br.com.blackbelt.api.mapper.PerfilMapper;
import br.com.blackbelt.domain.model.Perfil;
import br.com.blackbelt.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/perfis")
public class PerfilController {

    private final PerfilService perfilService;
    private final PerfilMapper perfilMapper;

    public PerfilController(
            PerfilService perfilService,
            PerfilMapper perfilMapper) {

        this.perfilService = perfilService;
        this.perfilMapper = perfilMapper;
    }

    @PreAuthorize("hasAuthority('PERFIL_LISTAR')")
    @GetMapping
    public List<PerfilResponse> listar() {

        return perfilService.listar()
                .stream()
                .map(perfilMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasAuthority('PERFIL_LISTAR')")
    @GetMapping("/{id}")
    public PerfilResponse buscarPorId(@PathVariable Long id) {

        Perfil perfil = perfilService.buscarPorId(id);

        return perfilMapper.toResponse(perfil);
    }

    @PreAuthorize("hasAuthority('PERFIL_CRIAR')")
    @PostMapping
    public ResponseEntity<PerfilResponse> cadastrar(
            @Valid @RequestBody PerfilRequest request) {

        Perfil perfil = new Perfil();

        perfil.setNome(request.nome());

        perfil = perfilService.cadastrar(
                perfil,
                request.permissaoIds()
        );

        PerfilResponse response =
                perfilMapper.toResponse(perfil);

        URI uri = URI.create(
                "/api/v1/perfis/" + perfil.getId()
        );

        return ResponseEntity.created(uri).body(response);
    }

    @PreAuthorize("hasAuthority('PERFIL_EDITAR')")
    @PutMapping("/{id}")
    public PerfilResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PerfilRequest request) {

        Perfil perfil = new Perfil();

        perfil.setNome(request.nome());

        perfil = perfilService.atualizar(
                id,
                perfil,
                request.permissaoIds()
        );

        return perfilMapper.toResponse(perfil);
    }

    @PreAuthorize("hasAuthority('PERFIL_EXCLUIR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        perfilService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}