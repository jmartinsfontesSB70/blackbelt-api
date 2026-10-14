package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.TipoCobrancaRequest;
import br.com.blackbelt.api.dto.TipoCobrancaResponse;
import br.com.blackbelt.service.TipoCobrancaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-cobranca")
public class TipoCobrancaController {

    private final TipoCobrancaService tipoCobrancaService;

    public TipoCobrancaController(
            TipoCobrancaService tipoCobrancaService
    ) {
        this.tipoCobrancaService = tipoCobrancaService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TIPO_COBRANCA_CADASTRAR')")
    public ResponseEntity<TipoCobrancaResponse> criar(
            @Valid @RequestBody TipoCobrancaRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tipoCobrancaService.criar(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('TIPO_COBRANCA_LISTAR')")
    public ResponseEntity<List<TipoCobrancaResponse>> listar() {

        return ResponseEntity.ok(
                tipoCobrancaService.listar()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TIPO_COBRANCA_LISTAR')")
    public ResponseEntity<TipoCobrancaResponse> buscarPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                tipoCobrancaService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TIPO_COBRANCA_EDITAR')")
    public ResponseEntity<TipoCobrancaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoCobrancaRequest request
    ) {

        return ResponseEntity.ok(
                tipoCobrancaService.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TIPO_COBRANCA_EXCLUIR')")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id
    ) {

        tipoCobrancaService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}