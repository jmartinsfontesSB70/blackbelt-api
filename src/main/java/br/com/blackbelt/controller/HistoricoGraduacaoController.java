package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.HistoricoGraduacaoRequest;
import br.com.blackbelt.api.dto.HistoricoGraduacaoResponse;
import br.com.blackbelt.service.HistoricoGraduacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/historico-graduacoes")
public class HistoricoGraduacaoController {

    private final HistoricoGraduacaoService service;

    public HistoricoGraduacaoController(
            HistoricoGraduacaoService service) {

        this.service = service;
    }

    @PostMapping
    public ResponseEntity<HistoricoGraduacaoResponse> salvar(
            @RequestBody @Valid HistoricoGraduacaoRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(request));
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<HistoricoGraduacaoResponse>> listarPorAluno(
            @PathVariable Long alunoId) {

        return ResponseEntity.ok(
                service.listarPorAluno(alunoId)
        );
    }
}