package br.com.blackbelt.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PresencaRequest {

    @NotNull(message = "A matrícula é obrigatória.")
    private Long matriculaId;

    @NotNull(message = "A data é obrigatória.")
    private LocalDate data;

    @NotNull(message = "A presença é obrigatória.")
    private Boolean presente;

    @Size(max = 300, message = "A observação deve ter no máximo 300 caracteres.")
    private String observacao;

    public Long getMatriculaId() {
        return matriculaId;
    }

    public void setMatriculaId(Long matriculaId) {
        this.matriculaId = matriculaId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Boolean getPresente() {
        return presente;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
