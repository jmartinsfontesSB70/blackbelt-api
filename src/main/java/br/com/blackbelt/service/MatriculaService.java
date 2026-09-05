package br.com.blackbelt.service;

import br.com.blackbelt.domain.model.Aluno;
import br.com.blackbelt.domain.model.Matricula;
import br.com.blackbelt.domain.model.Turma;
import br.com.blackbelt.domain.repository.MatriculaRepository;
import br.com.blackbelt.domain.repository.PresencaRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoService alunoService;
    private final TurmaService turmaService;
    private final PresencaRepository presencaRepository;

    public MatriculaService(
            MatriculaRepository matriculaRepository,
            AlunoService alunoService,
            TurmaService turmaService,
            PresencaRepository presencaRepository) {

        this.matriculaRepository = matriculaRepository;
        this.alunoService = alunoService;
        this.turmaService = turmaService;
        this.presencaRepository = presencaRepository;
    }

    public Page<Matricula> listar(Pageable pageable) {
        return matriculaRepository.findAll(pageable);
    }

    public Matricula buscarPorId(Long id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Matricula de código " + id + " não encontrada."));
    }

    public List<Matricula> listarAtivasPorTurma(
            Long turmaId,
            LocalDate data) {

        if (turmaId == null || turmaId <= 0) {
            throw new EntidadeConflitoException(
                    "A turma da chamada deve ser informada."
            );
        }

        if (data == null) {
            throw new EntidadeConflitoException(
                    "A data da chamada deve ser informada."
            );
        }

        return matriculaRepository
                .findByTurmaIdAndAtivaTrueAndDataMatriculaLessThanEqual(
                        turmaId,
                        data);
    }

    public Matricula cadastrar(Matricula matricula,
                           Long alunoId,
                           Long turmaId) {

        if (alunoId == null || alunoId <= 0) {
            throw new EntidadeConflitoException(
                    "O aluno da matrícula deve ser informado."
            );
        }

        if (turmaId == null || turmaId <= 0) {
            throw new EntidadeConflitoException(
                    "A turma da matrícula deve ser informada."
            );
        }

        Aluno aluno = alunoService.buscarPorId(alunoId);
        Turma turma = turmaService.buscarPorId(turmaId);

        validarAlunoAtivo(aluno);
        validarTurmaAtiva(turma);

        // Cadastro
        if (Boolean.TRUE.equals(matricula.getAtiva())) {
            validarMatriculaAtiva(alunoId, turmaId);
        }

        matricula.setAluno(aluno);
        matricula.setTurma(turma);

        return matriculaRepository.save(matricula);

    }

    public Matricula atualizar(Long id, Matricula matricula,
                           Long alunoId, Long turmaId) {

        Matricula matriculaAtual = buscarPorId(id);

        if (alunoId == null || alunoId <= 0) {
            throw new EntidadeConflitoException(
                    "O aluno da matrícula deve ser informado."
            );
        }

        if (turmaId == null || turmaId <= 0) {
            throw new EntidadeConflitoException(
                    "A turma da matrícula deve ser informada."
            );
        }

        Aluno aluno = alunoService.buscarPorId(alunoId);
        Turma turma = turmaService.buscarPorId(turmaId);

        validarAlunoAtivo(aluno);
        validarTurmaAtiva(turma);

        // Atualização

        if (Boolean.TRUE.equals(matricula.getAtiva())) {
            validarMatriculaAtiva(alunoId, turmaId, id);
        }

        matriculaAtual.setAluno(aluno);
        matriculaAtual.setTurma(turma);

        matriculaAtual.setDataMatricula(matricula.getDataMatricula());

        matriculaAtual.setAtiva(matricula.getAtiva());

        return matriculaRepository.save(matriculaAtual);
    }

    public void excluir(Long id) {

        Matricula matricula = buscarPorId(id);

        boolean possuiPresencas =
                presencaRepository.existsByMatriculaId(id);

        if (possuiPresencas) {
            throw new EntidadeConflitoException(
                    "Não é possível excluir esta matrícula porque existem registros de presença associados. " +
                            "Torne a matrícula inativa para preservar o histórico."
            );
        }

        matriculaRepository.delete(matricula);
    }

    private void validarAlunoAtivo(Aluno aluno) {

        if (!Boolean.TRUE.equals(aluno.getAtivo())) {
            throw new EntidadeConflitoException(
                    "Não é possível realizar a matrícula porque o aluno está inativo."
            );
        }
    }

    private void validarTurmaAtiva(Turma turma) {

        if (!Boolean.TRUE.equals(turma.getAtiva())) {
            throw new EntidadeConflitoException(
                    "Não é possível realizar a matrícula porque a turma está inativa."
            );
        }
    }

    private void validarMatriculaAtiva(Long alunoId, Long turmaId) {

        boolean existe = matriculaRepository
                .existsByAlunoIdAndTurmaIdAndAtivaTrue(alunoId, turmaId);

        if (existe) {
            throw new EntidadeConflitoException(
                    "O aluno já possui uma matrícula ativa nesta turma."
            );
        }
    }

    private void validarMatriculaAtiva(
            Long alunoId,
            Long turmaId,
            Long matriculaId) {

        boolean existe = matriculaRepository
                .existsByAlunoIdAndTurmaIdAndAtivaTrueAndIdNot(
                        alunoId,
                        turmaId,
                        matriculaId);

        if (existe) {
            throw new EntidadeConflitoException(
                    "O aluno já possui uma matrícula ativa nesta turma."
            );
        }
    }
}

