package br.com.blackbelt.api.dto;

import jakarta.validation.constraints.NotBlank;

public class RecuperacaoSenhaRequest {

    @NotBlank
    private String identificador;

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}