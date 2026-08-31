package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.AlunoRequest;
import br.com.blackbelt.api.dto.AlunoResponse;
import br.com.blackbelt.api.mapper.AlunoMapper;
import br.com.blackbelt.domain.model.Aluno;
import br.com.blackbelt.service.AlunoService;
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
@RequestMapping("/api/v1/alunos")
public class AlunoController {

    private final AlunoService alunoService;
    private final AlunoMapper alunoMapper;

    public AlunoController(AlunoService alunoService,
                           AlunoMapper alunoMapper) {

        this.alunoService = alunoService;
        this.alunoMapper = alunoMapper;
    }

    @PreAuthorize("hasAuthority('ALUNO_LISTAR')")
    @GetMapping
    public Page<AlunoResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.desc("id")
                )
        );

        Page<Aluno> alunos = alunoService.listar(pageable);

        return alunos.map(alunoMapper::toResponse);
    }

    @PreAuthorize("hasAuthority('ALUNO_CRIAR')")
    @PostMapping
    public ResponseEntity<AlunoResponse> cadastrar(@Valid @RequestBody AlunoRequest request) {

        Aluno aluno = alunoMapper.toEntity(request);

        aluno = alunoService.cadastrar(aluno);

        AlunoResponse response = alunoMapper.toResponse(aluno);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(aluno.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PreAuthorize("hasAuthority('ALUNO_EDITAR')")
    @PutMapping("/{id}")
    public AlunoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlunoRequest request) {

        Aluno aluno = alunoMapper.toEntity(request);

        aluno = alunoService.atualizar(id, aluno);

        return alunoMapper.toResponse(aluno);
    }

    @PreAuthorize("hasAuthority('ALUNO_LISTAR')")
    @GetMapping("/{id}")
    public AlunoResponse buscarPorId(@PathVariable Long id) {

        Aluno aluno = alunoService.buscarPorId(id);

        return alunoMapper.toResponse(aluno);
    }

    @PreAuthorize("hasAuthority('ALUNO_EXCLUIR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        alunoService.excluir(id);

        return ResponseEntity.noContent().build();
    }

}