package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.PresencaRequest;
import br.com.blackbelt.api.dto.PresencaResponse;
import br.com.blackbelt.api.mapper.PresencaMapper;
import br.com.blackbelt.domain.model.Presenca;
import br.com.blackbelt.service.PresencaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.security.access.prepost.PreAuthorize;

import br.com.blackbelt.api.dto.PresencaChamadaRequest;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/presencas")
public class PresencaController {

    private final PresencaService presencaService;
    private final PresencaMapper presencaMapper;

    public PresencaController(PresencaService presencaService,
                               PresencaMapper presencaMapper) {

        this.presencaService = presencaService;
        this.presencaMapper = presencaMapper;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRESENCA_LISTAR')")
    public Page<PresencaResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.desc("data"),
                        Sort.Order.desc("id")
                )
        );

        Page<Presenca> presencas =
                presencaService.listar(pageable);

        return presencas.map(presencaMapper::toResponse);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRESENCA_CRIAR')")
    public ResponseEntity<PresencaResponse> cadastrar(@Valid @RequestBody PresencaRequest request) {

        Presenca presenca = presencaMapper.toEntity(request);

        presenca = presencaService.cadastrar(
                presenca,
                request.getMatriculaId());

        PresencaResponse response = presencaMapper.toResponse(presenca);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(presenca.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/chamada")
    @PreAuthorize("hasAuthority('PRESENCA_CRIAR')")
    public List<PresencaResponse> registrarChamada(
            @Valid @RequestBody PresencaChamadaRequest request) {

        List<Presenca> presencas =
                presencaService.registrarChamada(request);

        return presencas.stream()
                .map(presencaMapper::toResponse)
                .toList();
    }

    @GetMapping("/chamada")
    @PreAuthorize("hasAuthority('PRESENCA_LISTAR')")
    public List<PresencaResponse> listarPorTurmaEData(
            @RequestParam Long turmaId,
            @RequestParam LocalDate data) {

        List<Presenca> presencas =
                presencaService.listarPorTurmaEData(
                        turmaId,
                        data);

        return presencas.stream()
                .map(presencaMapper::toResponse)
                .toList();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRESENCA_EDITAR')")
    public PresencaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PresencaRequest request) {

        Presenca presenca = presencaMapper.toEntity(request);

        presenca = presencaService.atualizar(
                id,
                presenca,
                request.getMatriculaId());

        return presencaMapper.toResponse(presenca);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PRESENCA_LISTAR')")
    public PresencaResponse buscarPorId(@PathVariable Long id) {

        Presenca presenca = presencaService.buscarPorId(id);

        return presencaMapper.toResponse(presenca);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRESENCA_EXCLUIR')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        presencaService.excluir(id);

        return ResponseEntity.noContent().build();
    }

}