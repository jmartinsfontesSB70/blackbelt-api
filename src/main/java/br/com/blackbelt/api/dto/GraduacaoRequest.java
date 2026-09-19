package br.com.blackbelt.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class GraduacaoRequest {

    @NotNull(message = "A modalidade é obrigatória.")
    private Long modalidadeId;

    @NotBlank(message = "O nome da graduação é obrigatório.")
    private String nome;

    @NotNull(message = "A ordem é obrigatória.")
    @PositiveOrZero(message = "A ordem deve ser maior ou igual a zero.")
    private Integer ordem;

    @NotNull(message = "A quantidade de graus é obrigatória.")
    @PositiveOrZero(message = "A quantidade de graus deve ser maior ou igual a zero.")
    private Integer quantidadeGraus;

    public Long getModalidadeId() {
        return modalidadeId;
    }

    public void setModalidadeId(Long modalidadeId) {
        this.modalidadeId = modalidadeId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Integer getQuantidadeGraus() {
        return quantidadeGraus;
    }

    public void setQuantidadeGraus(Integer quantidadeGraus) {
        this.quantidadeGraus = quantidadeGraus;
    }
}