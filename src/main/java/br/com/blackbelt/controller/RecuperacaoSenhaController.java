package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.RecuperacaoSenhaRequest;
import br.com.blackbelt.api.dto.RedefinirSenhaRequest;
import br.com.blackbelt.service.RecuperacaoSenhaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recuperacao-senha")
public class RecuperacaoSenhaController {

    private final RecuperacaoSenhaService recuperacaoSenhaService;

    public RecuperacaoSenhaController(
            RecuperacaoSenhaService recuperacaoSenhaService) {

        this.recuperacaoSenhaService = recuperacaoSenhaService;
    }

    @PostMapping
    public ResponseEntity<Void> solicitar(
            @Valid @RequestBody RecuperacaoSenhaRequest request,
            @RequestHeader(value = "X-Client", defaultValue = "web") String client) {

        recuperacaoSenhaService.gerarToken(
                request.getIdentificador(),
                client
        );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/redefinir")
    public ResponseEntity<Void> redefinir(
            @Valid @RequestBody RedefinirSenhaRequest request) {

        recuperacaoSenhaService.redefinirSenha(
                request.getToken(),
                request.getNovaSenha()
        );

        return ResponseEntity.noContent().build();
    }
}
