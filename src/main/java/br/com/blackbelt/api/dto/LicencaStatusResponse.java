package br.com.blackbelt.api.dto;

import java.time.LocalDate;

public class LicencaStatusResponse {

    private String situacao;
    private LocalDate dataExpiracao;
    private long diasRestantes;

    public LicencaStatusResponse(
            String situacao,
            LocalDate dataExpiracao,
            long diasRestantes) {
        this.situacao = situacao;
        this.dataExpiracao = dataExpiracao;
        this.diasRestantes = diasRestantes;
    }

    public String getSituacao() {
        return situacao;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public long getDiasRestantes() {
        return diasRestantes;
    }
}