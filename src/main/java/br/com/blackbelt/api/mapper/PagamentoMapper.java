package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.PagamentoResponse;
import br.com.blackbelt.domain.model.Pagamento;
import org.springframework.stereotype.Component;

@Component
public class PagamentoMapper {

    public PagamentoResponse toResponse(Pagamento pagamento) {

        PagamentoResponse response = new PagamentoResponse();

        response.setId(pagamento.getId());
        response.setCobrancaId(pagamento.getCobranca().getId());
        response.setDataPagamento(pagamento.getDataPagamento());
        response.setValor(pagamento.getValor());
        response.setFormaPagamento(pagamento.getFormaPagamento());
        response.setObservacao(pagamento.getObservacao());

        return response;
    }
}