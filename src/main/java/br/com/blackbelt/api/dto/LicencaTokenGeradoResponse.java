package br.com.blackbelt.api.dto;

import java.time.LocalDate;

public class LicencaTokenGeradoResponse {

    private String token;
    private int duracaoDias;
    private LocalDate dataGeracao;

    public LicencaTokenGeradoResponse(
            String token,
            int duracaoDias,
            LocalDate dataGeracao) {
        this.token = token;
        this.duracaoDias = duracaoDias;
        this.dataGeracao = dataGeracao;
    }

    public String getToken() {
        return token;
    }

    public int getDuracaoDias() {
        return duracaoDias;
    }

    public LocalDate getDataGeracao() {
        return dataGeracao;
    }
}