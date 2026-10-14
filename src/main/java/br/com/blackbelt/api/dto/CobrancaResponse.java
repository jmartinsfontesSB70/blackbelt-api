package br.com.blackbelt.api.dto;

import br.com.blackbelt.domain.model.StatusCobranca;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class CobrancaResponse {

    private Long id;

    private Long matriculaId;

    private Long alunoId;

    private String alunoNome;

    private Long tipoCobrancaId;

    private String tipoCobrancaNome;

    private String descricao;

    private YearMonth competencia;

    private LocalDate dataVencimento;

    private Integer quantidade;

    private BigDecimal valorUnitario;

    private BigDecimal valor;

    private StatusCobranca status;

    private LocalDate dataCancelamento;

    private String observacao;

    public CobrancaResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatriculaId() {
        return matriculaId;
    }

    public void setMatriculaId(Long matriculaId) {
        this.matriculaId = matriculaId;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public void setAlunoNome(String alunoNome) {
        this.alunoNome = alunoNome;
    }

    public Long getTipoCobrancaId() {
        return tipoCobrancaId;
    }

    public void setTipoCobrancaId(Long tipoCobrancaId) {
        this.tipoCobrancaId = tipoCobrancaId;
    }

    public String getTipoCobrancaNome() {
        return tipoCobrancaNome;
    }

    public void setTipoCobrancaNome(String tipoCobrancaNome) {
        this.tipoCobrancaNome = tipoCobrancaNome;
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