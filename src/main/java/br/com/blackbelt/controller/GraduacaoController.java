package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.GraduacaoRequest;
import br.com.blackbelt.api.dto.GraduacaoResponse;
import br.com.blackbelt.service.GraduacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/graduacoes")
public class GraduacaoController {

    private final GraduacaoService service;

    public GraduacaoController(GraduacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<GraduacaoResponse> salvar(
            @RequestBody @Valid GraduacaoRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(request));
    }

    @GetMapping("/modalidade/{modalidadeId}")
    public ResponseEntity<List<GraduacaoResponse>> listarPorModalidade(
            @PathVariable Long modalidadeId) {

        return ResponseEntity.ok(
                service.listarPorModalidade(modalidadeId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GraduacaoResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GraduacaoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid GraduacaoRequest request) {

        return ResponseEntity.ok(
                service.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}