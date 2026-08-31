package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.TurmaRequest;
import br.com.blackbelt.api.dto.TurmaResponse;
import br.com.blackbelt.domain.model.Turma;
import org.springframework.stereotype.Component;

@Component
public class TurmaMapper {

    public Turma toEntity(TurmaRequest request) {

        Turma turma = new Turma();

        turma.setNome(request.getNome());
        turma.setDiasSemana(request.getDiasSemana());
        turma.setHorarioInicio(request.getHorarioInicio());
        turma.setHorarioFim(request.getHorarioFim());
        turma.setCapacidade(request.getCapacidade());
        turma.setAtiva(request.getAtiva());

        return turma;
    }

    public TurmaResponse toResponse(Turma turma) {

        TurmaResponse response = new TurmaResponse();

        response.setId(turma.getId());
        response.setNome(turma.getNome());

        response.setModalidadeId(turma.getModalidade().getId());
        response.setModalidadeNome(turma.getModalidade().getNome());

        response.setProfessorId(turma.getProfessor().getId());
        response.setProfessorNome(turma.getProfessor().getNome());

        response.setDiasSemana(turma.getDiasSemana());
        response.setHorarioInicio(turma.getHorarioInicio());
        response.setHorarioFim(turma.getHorarioFim());
        response.setCapacidade(turma.getCapacidade());
        response.setAtiva(turma.getAtiva());

        return response;
    }
}

