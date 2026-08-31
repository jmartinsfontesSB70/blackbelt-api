package br.com.blackbelt.service;

import br.com.blackbelt.domain.model.Modalidade;
import br.com.blackbelt.domain.model.Professor;
import br.com.blackbelt.domain.model.Turma;
import br.com.blackbelt.domain.repository.MatriculaRepository;
import br.com.blackbelt.domain.repository.TurmaRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final ProfessorService professorService;
    private final ModalidadeService modalidadeService;
    private final MatriculaRepository matriculaRepository;

    public TurmaService(
            TurmaRepository turmaRepository,
            ProfessorService professorService,
            ModalidadeService modalidadeService,
            MatriculaRepository matriculaRepository) {

        this.turmaRepository = turmaRepository;
        this.professorService = professorService;
        this.modalidadeService = modalidadeService;
        this.matriculaRepository = matriculaRepository;
    }

    public Page<Turma> listar(Pageable pageable) {
        return turmaRepository.findAll(pageable);
    }

    public Turma buscarPorId(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Turma de código " + id + " não encontrada."));
    }

    public Turma cadastrar(Turma turma,
                           Long professorId,
                           Long modalidadeId) {

        if (professorId == null || professorId <= 0) {
            throw new EntidadeConflitoException(
                    "O professor da turma deve ser informado."
            );
        }

        if (modalidadeId == null || modalidadeId <= 0) {
            throw new EntidadeConflitoException(
                    "A modalidade da turma deve ser informada."
            );
        }

        Professor professor = professorService.buscarPorId(professorId);
        Modalidade modalidade = modalidadeService.buscarPorId(modalidadeId);

        turma.setProfessor(professor);
        turma.setModalidade(modalidade);

        validarHorario(turma);

        return turmaRepository.save(turma);

    }

    public Turma atualizar(Long id, Turma turma,
                           Long professorId, Long modalidadeId) {

        Turma turmaAtual = buscarPorId(id);

        if (professorId == null || professorId <= 0) {
            throw new EntidadeConflitoException(
                    "O professor da turma deve ser informado."
            );
        }

        if (modalidadeId == null || modalidadeId <= 0) {
            throw new EntidadeConflitoException(
                    "A modalidade da turma deve ser informada."
            );
        }

        Professor professor = professorService.buscarPorId(professorId);
        Modalidade modalidade = modalidadeService.buscarPorId(modalidadeId);

        turmaAtual.setProfessor(professor);
        turmaAtual.setModalidade(modalidade);

        turmaAtual.setNome(turma.getNome());
        turmaAtual.setDiasSemana(turma.getDiasSemana());
        turmaAtual.setHorarioInicio(turma.getHorarioInicio());
        turmaAtual.setHorarioFim(turma.getHorarioFim());
        turmaAtual.setCapacidade(turma.getCapacidade());
        turmaAtual.setAtiva(turma.getAtiva());

        validarHorario(turmaAtual);

        return turmaRepository.save(turmaAtual);
    }

    public void excluir(Long id) {

        Turma turma = buscarPorId(id);

        if (matriculaRepository.existsByTurmaId(id)) {
            throw new EntidadeConflitoException(
                    "Não é possível excluir a turma porque ela possui matrículas vinculadas.");
        }

        turmaRepository.delete(turma);
    }

    private void validarHorario(Turma turma) {

        if (!turma.getHorarioFim().isAfter(turma.getHorarioInicio())) {
            throw new EntidadeConflitoException(
                    "O horário final deve ser posterior ao horário inicial."
            );
        }
    }
}

