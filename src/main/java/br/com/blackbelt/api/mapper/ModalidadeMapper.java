package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.*;
import br.com.blackbelt.domain.model.Modalidade;
import org.springframework.stereotype.Component;

@Component
public class ModalidadeMapper {

    public Modalidade toEntity(ModalidadeRequest request) {

        Modalidade modalidade = new Modalidade();

        modalidade.setNome(request.getNome());
        modalidade.setDescricao(request.getDescricao());
        modalidade.setAtiva(request.getAtiva());

        return modalidade;
    }

    public ModalidadeResponse toResponse(Modalidade modalidade) {

        ModalidadeResponse response = new ModalidadeResponse();

        response.setId(modalidade.getId());
        response.setNome(modalidade.getNome());
        response.setDescricao(modalidade.getDescricao());
        response.setAtiva(modalidade.getAtiva());

        return response;
    }
}
