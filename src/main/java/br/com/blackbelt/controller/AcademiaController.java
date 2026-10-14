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
    @PutMapping
    public ResponseEntity<AcademiaResponse> atualizar(
            @Valid @RequestBody AcademiaRequest request) {

        return ResponseEntity.ok(
                academiaService.atualizar(request)
        );
    }
}
