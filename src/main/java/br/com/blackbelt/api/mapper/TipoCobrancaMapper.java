package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.TipoCobrancaResponse;
import br.com.blackbelt.domain.model.TipoCobranca;
import org.springframework.stereotype.Component;

@Component
public class TipoCobrancaMapper {

    public TipoCobrancaResponse toResponse(
            TipoCobranca tipoCobranca
    ) {

        TipoCobrancaResponse response =
                new TipoCobrancaResponse();

        response.setId(tipoCobranca.getId());
        response.setNome(tipoCobranca.getNome());
        response.setDescricao(tipoCobranca.getDescricao());
        response.setValorUnitario(
                tipoCobranca.getValorUnitario()
        );
        response.setAtivo(tipoCobranca.getAtivo());

        return response;
    }
}