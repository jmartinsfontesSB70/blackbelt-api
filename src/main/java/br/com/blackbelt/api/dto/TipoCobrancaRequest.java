package br.com.blackbelt.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TipoCobrancaRequest {

    @NotBlank(message = "O nome do tipo de cobrança é obrigatório.")
    private String nome;

    private String descricao;

    @NotNull(message = "O valor unitário é obrigatório.")
    @DecimalMin(
            value = "0.00",
            message = "O valor unitário não pode ser negativo."
    )
    private BigDecimal valorUnitario;

    @NotNull(message = "A situação do tipo de cobrança é obrigatória.")
    private Boolean ativo;

    public TipoCobrancaRequest() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}