package br.com.blackbelt.api.dto;

import java.time.LocalDate;

public class LicencaResponse {

    private String mensagem;
    private LocalDate dataInicio;
    private LocalDate dataExpiracao;

    public LicencaResponse(
            String mensagem,
            LocalDate dataInicio,
            LocalDate dataExpiracao) {
        this.mensagem = mensagem;
        this.dataInicio = dataInicio;
        this.dataExpiracao = dataExpiracao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }
}