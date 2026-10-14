package br.com.blackbelt.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LicencaGerarTokenRequest {

    @NotNull(message = "A duração da licença é obrigatória.")
    @Min(value = 1, message = "A duração deve ser de pelo menos 1 dia.")
    private Integer duracaoDias;

    public LicencaGerarTokenRequest() {
    }

    public Integer getDuracaoDias() {
        return duracaoDias;
    }

    public void setDuracaoDias(Integer duracaoDias) {
        this.duracaoDias = duracaoDias;
    }
}