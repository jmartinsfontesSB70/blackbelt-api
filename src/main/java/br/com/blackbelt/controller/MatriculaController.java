package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.MatriculaRequest;
import br.com.blackbelt.api.dto.MatriculaResponse;
import br.com.blackbelt.api.mapper.MatriculaMapper;
import br.com.blackbelt.domain.model.Matricula;
import br.com.blackbelt.service.MatriculaService;
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
import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;
    private final MatriculaMapper matriculaMapper;

    public MatriculaController(
            MatriculaService matriculaService,
            MatriculaMapper matriculaMapper) {

        this.matriculaService = matriculaService;
        this.matriculaMapper = matriculaMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MATRICULA_LISTAR')")
    public Page<MatriculaResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String pesquisa,
            @RequestParam(defaultValue = "dataMatricula") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        String campoOrdenacao = "dataMatricula";

        if ("id".equalsIgnoreCase(sort)) {
            campoOrdenacao = "id";
        }

        Sort.Direction direcao = "asc".equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Sort sortOrdenacao = Sort.by(
                direcao,
                campoOrdenacao
        );

        if ("dataMatricula".equalsIgnoreCase(campoOrdenacao)) {
            sortOrdenacao = sortOrdenacao.and(
                    Sort.by(Sort.Direction.DESC, "id")
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                sortOrdenacao
        );

        Page<Matricula> matriculas =
                matriculaService.listar(pageable, pesquisa);

        return matriculas.map(matriculaMapper::toResponse);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MATRICULA_CRIAR')")
    public ResponseEntity<MatriculaResponse> cadastrar(
            @Valid @RequestBody MatriculaRequest request) {

        Matricula matricula = matriculaMapper.toEntity(request);

        matricula = matriculaService.cadastrar(
                matricula,
                request.getAlunoId(),
                request.getTurmaId());

        MatriculaResponse response =
                matriculaMapper.toResponse(matricula);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(matricula.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MATRICULA_EDITAR')")
    public MatriculaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MatriculaRequest request) {

        Matricula matricula = matriculaMapper.toEntity(request);

        matricula = matriculaService.atualizar(
                id,
                matricula,
                request.getAlunoId(),
                request.getTurmaId());

        return matriculaMapper.toResponse(matricula);
    }

    @GetMapping("/turma/{turmaId}/ativas")
    @PreAuthorize("hasAuthority('MATRICULA_LISTAR')")
    public List<MatriculaResponse> listarAtivasPorTurma(
            @PathVariable Long turmaId,
            @RequestParam LocalDate data) {

        return matriculaService
                .listarAtivasPorTurma(turmaId, data)
                .stream()
                .map(matriculaMapper::toResponse)
                .toList();
    }

    @GetMapping("/aluno/{alunoId}/ativas")
    @PreAuthorize("hasAuthority('MATRICULA_LISTAR')")
    public List<MatriculaResponse> listarAtivasPorAluno(
            @PathVariable Long alunoId) {

        return matriculaService
                .listarAtivasPorAluno(alunoId)
                .stream()
                .map(matriculaMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MATRICULA_LISTAR')")
    public MatriculaResponse buscarPorId(@PathVariable Long id) {

        Matricula matricula =
                matriculaService.buscarPorId(id);

        return matriculaMapper.toResponse(matricula);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MATRICULA_EXCLUIR')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        matriculaService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}