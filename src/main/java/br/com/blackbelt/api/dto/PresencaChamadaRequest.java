package br.com.blackbelt.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class PresencaChamadaRequest {

    @NotNull(message = "A turma é obrigatória.")
    private Long turmaId;

    @NotNull(message = "A data é obrigatória.")
    private LocalDate data;

    @NotNull(message = "A lista de matrículas presentes é obrigatória.")
    private List<Long> matriculaIdsPresentes;

    public Long getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<Long> getMatriculaIdsPresentes() {
        return matriculaIdsPresentes;
    }

    public void setMatriculaIdsPresentes(List<Long> matriculaIdsPresentes) {
        this.matriculaIdsPresentes = matriculaIdsPresentes;
    }
}