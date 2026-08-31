package br.com.blackbelt.service;

import br.com.blackbelt.domain.model.Professor;
import br.com.blackbelt.domain.repository.ProfessorRepository;
import br.com.blackbelt.domain.repository.TurmaRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final TurmaRepository turmaRepository;

    public ProfessorService(
            ProfessorRepository professorRepository,
            TurmaRepository turmaRepository) {

        this.professorRepository = professorRepository;
        this.turmaRepository = turmaRepository;
    }

    public Page<Professor> listar(Pageable pageable) {
        return professorRepository.findAll(pageable);
    }

    public Professor buscarPorId(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                        "Professor de código " + id + " não encontrado."));
    }

    public Professor cadastrar(Professor professor) {

        if (professorRepository.existsByCpf(professor.getCpf())) {
            throw new EntidadeConflitoException(
                    "Já existe um professor cadastrado com este CPF."); }

        return professorRepository.save(professor);
    }

    public Professor atualizar(Long id, Professor professor) {

        Professor professorAtual = buscarPorId(id);

        if (!professorAtual.getCpf().equals(professor.getCpf())
                && professorRepository.existsByCpf(professor.getCpf())) {

            throw new EntidadeConflitoException(
                    "Já existe um professor cadastrado com este CPF.");
        }

        professorAtual.setNome(professor.getNome());
        professorAtual.setAtivo(professor.getAtivo());
        professorAtual.setCpf(professor.getCpf());
        professorAtual.setDataContratacao(professor.getDataContratacao());
        professorAtual.setDataNascimento(professor.getDataNascimento());
        professorAtual.setEmail(professor.getEmail());
        professorAtual.setTelefone(professor.getTelefone());
        professorAtual.setValorHoraAula(professor.getValorHoraAula());
        professorAtual.setEndereco(professor.getEndereco());

        return professorRepository.save(professorAtual);
    }

    public void excluir(Long id) {

        Professor professor = buscarPorId(id);

        if (turmaRepository.existsByProfessorId(id)) {
            throw new EntidadeConflitoException(
                    "Não é possível excluir este professor porque existem turmas associadas. Torne o professor inativo para preservar o histórico.");
        }

        professorRepository.delete(professor);
    }
}
