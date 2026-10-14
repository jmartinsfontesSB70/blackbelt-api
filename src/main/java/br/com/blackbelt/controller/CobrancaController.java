package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.CobrancaRequest;
import br.com.blackbelt.api.dto.CobrancaResponse;
import br.com.blackbelt.api.dto.PagamentoRequest;
import br.com.blackbelt.api.dto.PagamentoResponse;
import br.com.blackbelt.domain.model.Cobranca;
import br.com.blackbelt.api.mapper.CobrancaMapper;
import br.com.blackbelt.service.CobrancaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cobrancas")
public class CobrancaController {

    private final CobrancaService cobrancaService;
    private final CobrancaMapper cobrancaMapper;

    public CobrancaController(CobrancaService cobrancaService,
                              CobrancaMapper cobrancaMapper) {
        this.cobrancaService = cobrancaService;
        this.cobrancaMapper = cobrancaMapper;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COBRANCA_CADASTRAR')")
    public ResponseEntity<CobrancaResponse> criar(
            @Valid @RequestBody CobrancaRequest request
    ) {

        CobrancaResponse response =
                cobrancaService.criar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('COBRANCA_LISTAR')")
    public Page<CobrancaResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String pesquisa,
            @RequestParam(defaultValue = "dataVencimento") String sort,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        String campoOrdenacao = "dataVencimento";

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

        if ("dataVencimento".equalsIgnoreCase(campoOrdenacao)) {
            sortOrdenacao = sortOrdenacao.and(
                    Sort.by(Sort.Direction.DESC, "id")
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                sortOrdenacao
        );

        Page<Cobranca> cobrancas =
                cobrancaService.listar(pageable, pesquisa);

        return cobrancas.map(cobrancaMapper::toResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('COBRANCA_LISTAR')")
    public ResponseEntity<CobrancaResponse> buscarPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                cobrancaService.buscarPorId(id)
        );
    }

    @GetMapping("/matricula/{matriculaId}")
    @PreAuthorize("hasAuthority('COBRANCA_LISTAR')")
    public ResponseEntity<List<CobrancaResponse>> listarPorMatricula(
            @PathVariable Long matriculaId
    ) {

        return ResponseEntity.ok(
                cobrancaService.listarPorMatricula(matriculaId)
        );
    }

    @GetMapping("/{cobrancaId}/pagamentos")
    @PreAuthorize("hasAuthority('PAGAMENTO_LISTAR')")
    public ResponseEntity<List<PagamentoResponse>> listarPagamentos(
            @PathVariable Long cobrancaId
    ) {

        return ResponseEntity.ok(
                cobrancaService.listarPagamentos(cobrancaId)
        );
    }

    @DeleteMapping("/{cobrancaId}/pagamentos/{pagamentoId}")
    @PreAuthorize("hasAuthority('PAGAMENTO_EXCLUIR')")
    public ResponseEntity<Void> excluirPagamento(
            @PathVariable Long cobrancaId,
            @PathVariable Long pagamentoId
    ) {

        cobrancaService.excluirPagamento(
                cobrancaId,
                pagamentoId
        );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cobrancaId}/pagamentos")
    @PreAuthorize("hasAuthority('PAGAMENTO_CADASTRAR')")
    public ResponseEntity<PagamentoResponse> registrarPagamento(
            @PathVariable Long cobrancaId,
            @Valid @RequestBody PagamentoRequest request
    ) {

        request.setCobrancaId(cobrancaId);

        PagamentoResponse response =
                cobrancaService.registrarPagamento(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{cobrancaId}/cancelar")
    @PreAuthorize("hasAuthority('COBRANCA_CANCELAR')")
    public ResponseEntity<CobrancaResponse> cancelar(
            @PathVariable Long cobrancaId
    ) {

        return ResponseEntity.ok(
                cobrancaService.cancelar(cobrancaId)
        );
    }

    @DeleteMapping("/{cobrancaId}")
    @PreAuthorize("hasAuthority('COBRANCA_EXCLUIR')")
    public ResponseEntity<Void> excluir(
            @PathVariable Long cobrancaId
    ) {

        cobrancaService.excluir(cobrancaId);

        return ResponseEntity.noContent().build();
    }
}