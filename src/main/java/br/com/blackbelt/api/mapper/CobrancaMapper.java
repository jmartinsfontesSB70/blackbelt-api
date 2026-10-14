package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.CobrancaResponse;
import br.com.blackbelt.domain.model.Cobranca;
import org.springframework.stereotype.Component;

@Component
public class CobrancaMapper {

    public CobrancaResponse toResponse(Cobranca cobranca) {

        CobrancaResponse response = new CobrancaResponse();

        response.setId(cobranca.getId());

        response.setMatriculaId(
                cobranca.getMatricula().getId()
        );

        response.setAlunoId(
                cobranca.getMatricula()
                        .getAluno()
                        .getId()
        );

        response.setAlunoNome(
                cobranca.getMatricula()
                        .getAluno()
                        .getNome()
        );

        response.setTipoCobrancaId(
                cobranca.getTipoCobranca().getId()
        );

        response.setTipoCobrancaNome(
                cobranca.getTipoCobranca().getNome()
        );

        response.setDescricao(
                cobranca.getDescricao()
        );

        response.setCompetencia(
                cobranca.getCompetencia()
        );

        response.setDataVencimento(
                cobranca.getDataVencimento()
        );

        response.setQuantidade(
                cobranca.getQuantidade()
        );

        response.setValorUnitario(
                cobranca.getValorUnitario()
        );

        response.setValor(
                cobranca.getValor()
        );

        response.setStatus(
                cobranca.getStatus()
        );

        response.setDataCancelamento(
                cobranca.getDataCancelamento()
        );

        response.setObservacao(
                cobranca.getObservacao()
        );

        return response;
    }
}