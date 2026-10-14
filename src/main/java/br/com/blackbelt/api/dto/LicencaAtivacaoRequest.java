package br.com.blackbelt.api.dto;

import jakarta.validation.constraints.NotBlank;

public class LicencaAtivacaoRequest {

    @NotBlank(message = "O token de ativação é obrigatório.")
    private String token;

    public LicencaAtivacaoRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}