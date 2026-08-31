package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.MatriculaRequest;
import br.com.blackbelt.api.dto.MatriculaResponse;
import br.com.blackbelt.domain.model.Matricula;
import org.springframework.stereotype.Component;

@Component
public class MatriculaMapper {

    public Matricula toEntity(MatriculaRequest request) {

        Matricula matricula = new Matricula();

        matricula.setDataMatricula(request.getDataMatricula());
        matricula.setAtiva(request.getAtiva());

        return matricula;
    }

    public MatriculaResponse toResponse(Matricula matricula) {

        MatriculaResponse response = new MatriculaResponse();

        response.setId(matricula.getId());
        response.setDataMatricula(matricula.getDataMatricula());

        if (matricula.getAluno() != null) {

            response.setAlunoId(matricula.getAluno().getId());
            response.setAlunoNome(matricula.getAluno().getNome());
        }

        if (matricula.getTurma() != null) {

            response.setTurmaId(matricula.getTurma().getId());
            response.setTurmaNome(matricula.getTurma().getNome());
        }

        response.setAtiva(matricula.getAtiva());

        return response;
    }
}


