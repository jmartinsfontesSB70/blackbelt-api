package br.com.blackbelt.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name = "cobrancas")
public class Cobranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A matrícula é obrigatória.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_id", nullable = false)
    private Matricula matricula;

    @NotNull(message = "O tipo de cobrança é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_cobranca_id", nullable = false)
    private TipoCobranca tipoCobranca;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @Convert(converter = YearMonthConverter.class)
    private YearMonth competencia;

    @NotNull(message = "A data de vencimento é obrigatória.")
    private LocalDate dataVencimento;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    private Integer quantidade;

    @NotNull(message = "O valor unitário é obrigatório.")
    @DecimalMin(
            value = "0.01",
            message = "O valor unitário deve ser maior que zero."
    )
    private BigDecimal valorUnitario;

    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(
            value = "0.01",
            message = "O valor deve ser maior que zero."
    )
    private BigDecimal valor;

    @NotNull(message = "O status é obrigatório.")
    @Enumerated(EnumType.STRING)
    private StatusCobranca status;

    private LocalDate dataCancelamento;

    private String observacao;

    public Cobranca() {
    }

    public Long getId() {
        return id;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public TipoCobranca getTipoCobranca() {
        return tipoCobranca;
    }

    public void setTipoCobranca(TipoCobranca tipoCobranca) {
        this.tipoCobranca = tipoCobranca;
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

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public StatusCobranca getStatus() {
        return status;
    }

    public void setStatus(StatusCobranca status) {
        this.status = status;
    }

    public LocalDate getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(LocalDate dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}