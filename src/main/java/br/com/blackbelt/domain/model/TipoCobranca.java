package br.com.blackbelt.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "tipos_cobranca")
public class TipoCobranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do tipo de cobrança é obrigatório.")
    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;

    @NotNull(message = "O valor unitário é obrigatório.")
    @DecimalMin(
            value = "0.00",
            message = "O valor unitário não pode ser negativo."
    )
    @Column(nullable = false)
    private BigDecimal valorUnitario;

    @NotNull(message = "A situação do tipo de cobrança é obrigatória.")
    @Column(nullable = false)
    private Boolean ativo;

    public TipoCobranca() {
    }

    public Long getId() {
        return id;
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