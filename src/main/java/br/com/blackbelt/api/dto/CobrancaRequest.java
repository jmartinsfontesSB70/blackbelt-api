package br.com.blackbelt.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.YearMonth;

public class CobrancaRequest {

    @NotNull(message = "A matrícula é obrigatória.")
    private Long matriculaId;

    @NotNull(message = "O tipo de cobrança é obrigatório.")
    private Long tipoCobrancaId;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    private YearMonth competencia;

    @NotNull(message = "A data de vencimento é obrigatória.")
    private LocalDate dataVencimento;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    private Integer quantidade;

    private String observacao;

    public CobrancaRequest() {
    }

    public Long getMatriculaId() {
        return matriculaId;
    }

    public void setMatriculaId(Long matriculaId) {
        this.matriculaId = matriculaId;
    }

    public Long getTipoCobrancaId() {
        return tipoCobrancaId;
    }

    public void setTipoCobrancaId(Long tipoCobrancaId) {
        this.tipoCobrancaId = tipoCobrancaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public YearMonth getCompetencia() {
        return competencia;
    }

    public void setCompetencia(YearMonth competencia) {
        this.competencia = competencia;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}