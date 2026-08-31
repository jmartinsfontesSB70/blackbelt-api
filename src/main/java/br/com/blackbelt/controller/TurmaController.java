package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.TurmaRequest;
import br.com.blackbelt.api.dto.TurmaResponse;
import br.com.blackbelt.api.mapper.TurmaMapper;
import br.com.blackbelt.domain.model.Turma;
import br.com.blackbelt.service.TurmaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.security.access.prepost.PreAuthorize;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/turmas")
public class TurmaController {

    private final TurmaService turmaService;
    private final TurmaMapper turmaMapper;

    public TurmaController(TurmaService turmaService,
                               TurmaMapper turmaMapper) {

        this.turmaService = turmaService;
        this.turmaMapper = turmaMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('TURMA_LISTAR')")
    public Page<TurmaResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.desc("id")
                )
        );

        Page<Turma> turmas = turmaService.listar(pageable);

        return turmas.map(turmaMapper::toResponse);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TURMA_CRIAR')")
    public ResponseEntity<TurmaResponse> cadastrar(@Valid @RequestBody TurmaRequest request) {

        Turma turma = turmaMapper.toEntity(request);

        turma = turmaService.cadastrar(
                turma,
                request.getProfessorId(),
                request.getModalidadeId());

        TurmaResponse response = turmaMapper.toResponse(turma);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(turma.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TURMA_EDITAR')")
    public TurmaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TurmaRequest request) {

        Turma turma = turmaMapper.toEntity(request);

        turma = turmaService.atualizar(
                id,
                turma,
                request.getProfessorId(),
                request.getModalidadeId());

        return turmaMapper.toResponse(turma);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TURMA_LISTAR')")
    public TurmaResponse buscarPorId(@PathVariable Long id) {

        Turma turma = turmaService.buscarPorId(id);

        return turmaMapper.toResponse(turma);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TURMA_EXCLUIR')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        turmaService.excluir(id);

        return ResponseEntity.noContent().build();
    }

}