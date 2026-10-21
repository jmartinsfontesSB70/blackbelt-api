package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.AcademiaRequest;
import br.com.blackbelt.api.dto.AcademiaResponse;
import br.com.blackbelt.service.AcademiaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.http.HttpHeaders;
import java.io.IOException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/academia")
public class AcademiaController {

    private final AcademiaService academiaService;

    public AcademiaController(AcademiaService academiaService) {
        this.academiaService = academiaService;
    }

    /**
     * Verifica se a instalação já possui uma academia cadastrada.
     */
    @GetMapping("/status-instalacao")
    public ResponseEntity<Map<String, Boolean>> verificarInstalacao() {

        boolean cadastrada = academiaService.existeAcademia();

        return ResponseEntity.ok(
                Map.of("academiaCadastrada", cadastrada)
        );
    }

    /**
     * Realiza o cadastro inicial da academia.
     * O logotipo é opcional e deve ser enviado em JPG.
     */
    @PostMapping(
            value = "/instalacao",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AcademiaResponse> cadastrar(
            @Valid @RequestPart("academia") AcademiaRequest request,
            @RequestPart(value = "logo", required = false)
            MultipartFile logo) {

        AcademiaResponse response =
                academiaService.cadastrar(request, logo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Consulta os dados da academia cadastrada.
     */
    @GetMapping
    public ResponseEntity<AcademiaResponse> buscar() {

        return ResponseEntity.ok(
                academiaService.buscar()
        );
    }

    /**
     * Atualiza os dados cadastrais da academia.
     * Não altera as datas da avaliação nem o logotipo.
     */
    @PreAuthorize("hasAuthority('ACADEMIA_EDITAR')")
    @PutMapping
    public ResponseEntity<AcademiaResponse> atualizar(
            @Valid @RequestBody AcademiaRequest request) {

        return ResponseEntity.ok(
                academiaService.atualizar(request)
        );
    }

    @PreAuthorize("hasAuthority('ACADEMIA_EDITAR')")
    @PutMapping(
            value = "/logo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AcademiaResponse> atualizarLogo(
            @RequestPart("logo") MultipartFile logo) {

        return ResponseEntity.ok(
                academiaService.atualizarLogo(logo)
        );
    }

    @GetMapping("/logo")
    public ResponseEntity<byte[]> buscarLogo() {
        AcademiaResponse academia = academiaService.buscar();

        // O método buscar() retorna os dados cadastrais.
        // Os bytes da logo são obtidos pelo método específico abaixo.
        byte[] imagem = academiaService.buscarLogo();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                org.springframework.http.MediaType.IMAGE_JPEG
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(imagem);
    }

}
