package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.*;
import br.com.blackbelt.api.mapper.UsuarioMapper;
import br.com.blackbelt.domain.model.Usuario;
import br.com.blackbelt.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(
            UsuarioService usuarioService,
            UsuarioMapper usuarioMapper) {

        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @PreAuthorize("hasAuthority('USUARIO_LISTAR')")
    @GetMapping
    public Page<UsuarioResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String pesquisa,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        String campoOrdenacao = "id";

        if ("username".equalsIgnoreCase(sort)) {
            campoOrdenacao = "username";
        } else if ("email".equalsIgnoreCase(sort)) {
            campoOrdenacao = "email";
        }

        Sort.Direction direcao =
                "asc".equalsIgnoreCase(direction)
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direcao, campoOrdenacao)
        );

        Page<Usuario> usuarios =
                usuarioService.listar(pageable, pesquisa);

        return usuarios.map(usuarioMapper::toResponse);
    }

    @PreAuthorize("hasAuthority('USUARIO_LISTAR')")
    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {

        Usuario usuario = usuarioService.buscarPorId(id);

        return usuarioMapper.toResponse(usuario);
    }

    @PreAuthorize("hasAuthority('USUARIO_CRIAR')")
    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(
            @Valid @RequestBody UsuarioRequest request) {

        Usuario usuario = usuarioMapper.toEntity(request);

        usuario = usuarioService.cadastrar(
                usuario,
                request.getPassword(),
                request.getPerfilId()
        );

        UsuarioResponse response =
                usuarioMapper.toResponse(usuario);

        URI uri = URI.create(
                "/api/v1/usuarios/" + usuario.getId()
        );

        return ResponseEntity.created(uri).body(response);
    }

    @PreAuthorize("hasAuthority('USUARIO_EDITAR')")
    @PutMapping("/{id}")
    public UsuarioResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioAtualizacaoRequest request) {

        Usuario usuario = usuarioMapper.toEntity(request);

        usuario = usuarioService.atualizar(
                id,
                usuario,
                request.getPassword(),
                request.getPerfilId()
        );

        return usuarioMapper.toResponse(usuario);
    }

    @PreAuthorize("hasAuthority('USUARIO_EDITAR')")
    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioSenhaRequest request) {

        usuarioService.alterarSenha(
                id,
                request.getNovaSenha()
        );

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/minha-senha")
    public ResponseEntity<Void> alterarMinhaSenha(
            @Valid @RequestBody AlterarSenhaRequest request,
            Authentication authentication) {

        usuarioService.alterarMinhaSenha(
                authentication.getName(),
                request.getSenhaAtual(),
                request.getNovaSenha()
        );

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('USUARIO_EXCLUIR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        usuarioService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}