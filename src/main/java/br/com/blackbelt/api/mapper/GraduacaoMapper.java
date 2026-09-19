package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.GraduacaoResponse;
import br.com.blackbelt.domain.model.Graduacao;
import org.springframework.stereotype.Component;

@Component
public class GraduacaoMapper {

    public GraduacaoResponse toResponse(Graduacao graduacao) {

        GraduacaoResponse response = new GraduacaoResponse();

        response.setId(graduacao.getId());
        response.setModalidadeId(graduacao.getModalidade().getId());
        response.setModalidadeNome(graduacao.getModalidade().getNome());
        response.setNome(graduacao.getNome());
        response.setOrdem(graduacao.getOrdem());
        response.setQuantidadeGraus(graduacao.getQuantidadeGraus());

        return response;
    }
}