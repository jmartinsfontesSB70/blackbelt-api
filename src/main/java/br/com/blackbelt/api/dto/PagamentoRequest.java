package br.com.blackbelt.api.dto;

import br.com.blackbelt.domain.model.FormaPagamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PagamentoRequest {

    private Long cobrancaId;

    @NotNull(message = "A data do pagamento é obrigatória.")
    private LocalDate dataPagamento;

    @NotNull(message = "O valor do pagamento é obrigatório.")
    @DecimalMin(
            value = "0.01",
            message = "O valor do pagamento deve ser maior que zero."
    )
    private BigDecimal valor;

    @NotNull(message = "A forma de pagamento é obrigatória.")
    private FormaPagamento formaPagamento;

    private String observacao;

    public PagamentoRequest() {
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