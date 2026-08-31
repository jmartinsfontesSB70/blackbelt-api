package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.PresencaRequest;
import br.com.blackbelt.api.dto.PresencaResponse;
import br.com.blackbelt.domain.model.Presenca;
import org.springframework.stereotype.Component;

@Component
public class PresencaMapper {

    public Presenca toEntity(PresencaRequest request) {

        Presenca presenca = new Presenca();

        presenca.setData(request.getData());
        presenca.setPresente(request.getPresente());
        presenca.setObservacao(request.getObservacao());

        return presenca;
    }

    public PresencaResponse toResponse(Presenca presenca) {

        PresencaResponse response = new PresencaResponse();

        response.setId(presenca.getId());
        response.setData(presenca.getData());

        if (presenca.getMatricula() != null) {

            response.setMatriculaId(
                    presenca.getMatricula().getId());

            if (presenca.getMatricula().getAluno() != null) {

                response.setAlunoNome(
                        presenca.getMatricula().getAluno().getNome()
                );
            }
        }

        if (presenca.getMatricula() != null &&
                presenca.getMatricula().getTurma() != null) {

            response.setTurmaNome(
                    presenca.getMatricula()
                            .getTurma()
                            .getNome()
            );
        }

        response.setObservacao(presenca.getObservacao());
        response.setPresente(presenca.getPresente());

        return response;
    }
}
