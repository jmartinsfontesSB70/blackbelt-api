package br.com.blackbelt.controller;

import br.com.blackbelt.domain.StatusApi;
import br.com.blackbelt.service.StatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/api/v1")
    public String boasVindas() {
        return "Bem-vindo à BlackBelt API!";
    }

    @GetMapping("/api/v1/status")
    public StatusApi status() {
        return statusService.obterStatus();
    }
}