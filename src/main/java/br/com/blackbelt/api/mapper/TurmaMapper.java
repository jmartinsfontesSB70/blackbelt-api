package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.TurmaRequest;
import br.com.blackbelt.api.dto.TurmaResponse;
import br.com.blackbelt.domain.model.Turma;
import br.com.blackbelt.domain.model.TurmaDia;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TurmaMapper {

    public Turma toEntity(TurmaRequest request) {

        Turma turma = new Turma();

        turma.setNome(request.getNome());
        turma.setHorarioInicio(request.getHorarioInicio());
        turma.setHorarioFim(request.getHorarioFim());
        turma.setCapacidade(request.getCapacidade());
        turma.setAtiva(request.getAtiva());

        if (request.getDiasSemana() != null) {

            List<TurmaDia> dias = request.getDiasSemana()
                    .stream()
                    .map(diaSemana -> {

                        TurmaDia turmaDia = new TurmaDia();

                        turmaDia.setTurma(turma);
                        turmaDia.setDiaSemana(diaSemana);

                        return turmaDia;
                    })
                    .toList();

            turma.setDias(dias);
        }

        return turma;
    }

    public TurmaResponse toResponse(Turma turma) {

        TurmaResponse response = new TurmaResponse();

        response.setId(turma.getId());
        response.setNome(turma.getNome());

        response.setModalidadeId(
                turma.getModalidade().getId()
        );

        response.setModalidadeNome(
                turma.getModalidade().getNome()
        );

        response.setProfessorId(
                turma.getProfessor().getId()
        );

        response.setProfessorNome(
                turma.getProfessor().getNome()
        );

        response.setDiasSemana(
                turma.getDias()
                        .stream()
                        .map(TurmaDia::getDiaSemana)
                        .toList()
        );

        response.setHorarioInicio(turma.getHorarioInicio());
        response.setHorarioFim(turma.getHorarioFim());
        response.setCapacidade(turma.getCapacidade());
        response.setAtiva(turma.getAtiva());

        return response;
    }
}