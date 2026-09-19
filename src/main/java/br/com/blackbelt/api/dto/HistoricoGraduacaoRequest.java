package br.com.blackbelt.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public class HistoricoGraduacaoRequest {

    @NotNull(message = "O aluno é obrigatório.")
    private Long alunoId;

    @NotNull(message = "A graduação é obrigatória.")
    private Long graduacaoId;

    @NotNull(message = "O grau é obrigatório.")
    @PositiveOrZero(message = "O grau deve ser maior ou igual a zero.")
    private Integer grau;

    @NotNull(message = "A data é obrigatória.")
    private LocalDate data;

    private String observacao;

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getGraduacaoId() {
        return graduacaoId;
    }

    public void setGraduacaoId(Long graduacaoId) {
        this.graduacaoId = graduacaoId;
    }

    public Integer getGrau() {
        return grau;
    }

    public void setGrau(Integer grau) {
        this.grau = grau;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}