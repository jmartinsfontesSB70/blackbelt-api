package br.com.blackbelt.controller;

import br.com.blackbelt.api.dto.EnderecoCepResponse;
import br.com.blackbelt.service.ViaCepService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ceps")
public class ViaCepController {

    private final ViaCepService viaCepService;

    public ViaCepController(ViaCepService viaCepService) {
        this.viaCepService = viaCepService;
    }

    @GetMapping("/{cep}")
    public EnderecoCepResponse buscarCep(@PathVariable String cep) {
        return viaCepService.buscarCep(cep);
    }
}