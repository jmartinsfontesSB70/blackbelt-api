package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.HistoricoGraduacaoResponse;
import br.com.blackbelt.domain.model.HistoricoGraduacao;
import org.springframework.stereotype.Component;

@Component
public class HistoricoGraduacaoMapper {

    public HistoricoGraduacaoResponse toResponse(
            HistoricoGraduacao historico) {

        HistoricoGraduacaoResponse response =
                new HistoricoGraduacaoResponse();

        response.setId(historico.getId());
        response.setAlunoId(historico.getAluno().getId());
        response.setAlunoNome(historico.getAluno().getNome());
        response.setGraduacaoId(historico.getGraduacao().getId());
        response.setGraduacaoNome(historico.getGraduacao().getNome());
        response.setModalidadeId(
                historico.getGraduacao().getModalidade().getId()
        );
        response.setModalidadeNome(
                historico.getGraduacao().getModalidade().getNome()
        );
        response.setGrau(historico.getGrau());
        response.setData(historico.getData());
        response.setObservacao(historico.getObservacao());

        return response;
    }
}