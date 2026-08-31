package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.ProfessorRequest;
import br.com.blackbelt.api.dto.ProfessorResponse;
import br.com.blackbelt.api.mapper.ProfessorMapper;
import br.com.blackbelt.domain.model.Professor;
import br.com.blackbelt.service.ProfessorService;
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
@RequestMapping("/api/v1/professores")
public class ProfessorController {

    private final ProfessorService professorService;
    private final ProfessorMapper professorMapper;

    public ProfessorController(ProfessorService professorService,
                               ProfessorMapper professorMapper) {

        this.professorService = professorService;
        this.professorMapper = professorMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PROFESSOR_LISTAR')")
    public Page<ProfessorResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.desc("id")
                )
        );

        Page<Professor> professores =
                professorService.listar(pageable);

        return professores.map(professorMapper::toResponse);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PROFESSOR_CRIAR')")
    public ResponseEntity<ProfessorResponse> cadastrar(@Valid @RequestBody ProfessorRequest request) {

        Professor professor = professorMapper.toEntity(request);

        professor = professorService.cadastrar(professor);

        ProfessorResponse response = professorMapper.toResponse(professor);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(professor.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PROFESSOR_EDITAR')")
    public ProfessorResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfessorRequest request) {

        Professor professor = professorMapper.toEntity(request);

        professor = professorService.atualizar(id, professor);

        return professorMapper.toResponse(professor);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PROFESSOR_LISTAR')")
    public ProfessorResponse buscarPorId(@PathVariable Long id) {

        Professor professor = professorService.buscarPorId(id);

        return professorMapper.toResponse(professor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PROFESSOR_EXCLUIR')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        professorService.excluir(id);

        return ResponseEntity.noContent().build();
    }

}