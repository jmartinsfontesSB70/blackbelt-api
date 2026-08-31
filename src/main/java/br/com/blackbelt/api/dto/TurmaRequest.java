package br.com.blackbelt.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public class TurmaRequest {

    @NotBlank(message = "O nome da turma é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    private String nome;

    @NotNull(message = "A modalidade é obrigatória.")
    private Long modalidadeId;

    @NotNull(message = "O professor é obrigatório.")
    private Long professorId;

    @NotBlank(message = "Os dias da semana são obrigatórios.")
    @Size(max = 50, message = "Informe no máximo 50 caracteres.")
    private String diasSemana;

    @NotNull(message = "Horário inicial é obrigatório.")
    private LocalTime horarioInicio;

    @NotNull(message = "Horário final é obrigatório.")
    private LocalTime horarioFim;

    @NotNull(message = "Capacidade é obrigatória.")
    @Min(value = 1, message = "A capacidade deve ser maior que zero.")
    private Integer capacidade;

    @NotNull(message = "Situação da turma é obrigatória.")
    private Boolean ativa;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getModalidadeId() {
        return modalidadeId;
    }

    public void setModalidadeId(Long modalidadeId) {
        this.modalidadeId = modalidadeId;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }

    public String getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(String diasSemana) {
        this.diasSemana = diasSemana;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}
