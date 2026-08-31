package br.com.blackbelt.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class MatriculaRequest {

    @NotNull(message = "O aluno é obrigatório.")
    private Long alunoId;

    @NotNull(message = "A turma é obrigatória.")
    private Long turmaId;

    @NotNull(message = "A data da matrícula é obrigatória.")
    private LocalDate dataMatricula;

    @NotNull(message = "Situação da matrícula é obrigatória.")
    private Boolean ativa;

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDate dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}
