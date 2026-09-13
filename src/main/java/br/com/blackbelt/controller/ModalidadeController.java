package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.ModalidadeRequest;
import br.com.blackbelt.api.dto.ModalidadeResponse;
import br.com.blackbelt.api.mapper.ModalidadeMapper;
import br.com.blackbelt.domain.model.Modalidade;
import br.com.blackbelt.service.ModalidadeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/modalidades")
public class ModalidadeController {

    private final ModalidadeService modalidadeService;
    private final ModalidadeMapper modalidadeMapper;

    public ModalidadeController(ModalidadeService modalidadeService,
                                ModalidadeMapper modalidadeMapper) {

        this.modalidadeService = modalidadeService;
        this.modalidadeMapper = modalidadeMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MODALIDADE_LISTAR')")
    public Page<ModalidadeResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String pesquisa,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        String campoOrdenacao = "id";

        if ("nome".equalsIgnoreCase(sort)) {
            campoOrdenacao = "nome";
        }

        Sort.Direction direcao = "asc".equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direcao, campoOrdenacao)
        );

        Page<Modalidade> modalidades =
                modalidadeService.listar(pageable, pesquisa);

        return modalidades.map(modalidadeMapper::toResponse);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MODALIDADE_CRIAR')")
    public ResponseEntity<ModalidadeResponse> cadastrar(@Valid @RequestBody ModalidadeRequest request) {

        Modalidade modalidade = modalidadeMapper.toEntity(request);

        modalidade = modalidadeService.cadastrar(modalidade);

        ModalidadeResponse response = modalidadeMapper.toResponse(modalidade);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(modalidade.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MODALIDADE_EDITAR')")
    public ModalidadeResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ModalidadeRequest request) {

        Modalidade modalidade = modalidadeMapper.toEntity(request);

        modalidade = modalidadeService.atualizar(id, modalidade);

        return modalidadeMapper.toResponse(modalidade);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MODALIDADE_LISTAR')")
    public ModalidadeResponse buscarPorId(@PathVariable Long id) {

        Modalidade modalidade = modalidadeService.buscarPorId(id);

        return modalidadeMapper.toResponse(modalidade);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MODALIDADE_EXCLUIR')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        modalidadeService.excluir(id);

        return ResponseEntity.noContent().build();
    }

}