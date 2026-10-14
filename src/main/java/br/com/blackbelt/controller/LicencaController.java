package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.LicencaAtivacaoRequest;
import br.com.blackbelt.api.dto.LicencaGerarTokenRequest;
import br.com.blackbelt.api.dto.LicencaResponse;
import br.com.blackbelt.api.dto.LicencaTokenGeradoResponse;
import br.com.blackbelt.domain.model.Licenca;
import br.com.blackbelt.service.LicencaService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/api/v1/licenca")
public class LicencaController {

    private final LicencaService licencaService;
    private final String chaveEmissao;

    public LicencaController(
            LicencaService licencaService,
            @Value("${blackbelt.licenca.chave-emissao:}")
            String chaveEmissao) {

        this.licencaService = licencaService;
        this.chaveEmissao = chaveEmissao;
    }

    @PostMapping("/tokens")
    public ResponseEntity<LicencaTokenGeradoResponse> gerarToken(
            @RequestHeader(
                    value = "X-BlackBelt-Emissor",
                    required = false
            ) String chaveInformada,
            @Valid @RequestBody LicencaGerarTokenRequest request) {

        validarChaveEmissao(chaveInformada);

        LicencaTokenGeradoResponse resposta =
                licencaService.gerarToken(
                        request.getDuracaoDias()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resposta);
    }

    @PostMapping("/ativar")
    public ResponseEntity<LicencaResponse> ativar(
            @Valid @RequestBody LicencaAtivacaoRequest request) {

        Licenca licenca = licencaService.ativar(
                request.getToken()
        );

        LicencaResponse resposta = new LicencaResponse(
                "Licença ativada com sucesso.",
                licenca.getDataInicio(),
                licenca.getDataExpiracao()
        );

        return ResponseEntity.ok(resposta);
    }

    private void validarChaveEmissao(String chaveInformada) {

        if (chaveEmissao.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "A emissão de tokens não está configurada."
            );
        }

        if (chaveInformada == null
                || !MessageDigest.isEqual(
                chaveEmissao.getBytes(StandardCharsets.UTF_8),
                chaveInformada.getBytes(StandardCharsets.UTF_8))) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Não autorizado a gerar tokens."
            );
        }
    }
}