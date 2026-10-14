package br.com.blackbelt.api.dto;

import br.com.blackbelt.domain.model.FormaPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PagamentoResponse {

    private Long id;
    private Long cobrancaId;
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private FormaPagamento formaPagamento;
    private String observacao;

    public PagamentoResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCobrancaId() {
        return cobrancaId;
    }

    public void setCobrancaId(Long cobrancaId) {
        this.cobrancaId = cobrancaId;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}