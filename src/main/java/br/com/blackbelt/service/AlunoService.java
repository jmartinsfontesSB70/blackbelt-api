package br.com.blackbelt.service;

import br.com.blackbelt.domain.model.Aluno;
import br.com.blackbelt.domain.repository.AlunoRepository;
import br.com.blackbelt.domain.repository.MatriculaRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final MatriculaRepository matriculaRepository;

    public AlunoService(
            AlunoRepository alunoRepository,
            MatriculaRepository matriculaRepository) {

        this.alunoRepository = alunoRepository;
        this.matriculaRepository = matriculaRepository;
    }

    public Page<Aluno> listar(Pageable pageable) {
        return alunoRepository.findAll(pageable);
    }

    public Aluno cadastrar(Aluno aluno) {

        if (alunoRepository.existsByCpf(aluno.getCpf())) {
            throw new EntidadeConflitoException(
                    "Já existe um aluno cadastrado com este CPF."); }

        return alunoRepository.save(aluno);
    }

    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Aluno de código " + id + " não encontrado."));
    }

    public Aluno atualizar(Long id, Aluno aluno) {

        Aluno alunoExistente = buscarPorId(id);

        if (alunoRepository.existsByCpfAndIdNot(aluno.getCpf(), id)) {
            throw new EntidadeConflitoException(
                    "Já existe um aluno cadastrado com este CPF.");
        }

        alunoExistente.setNome(aluno.getNome());
        alunoExistente.setDataNascimento(aluno.getDataNascimento());
        alunoExistente.setCpf(aluno.getCpf());
        alunoExistente.setTelefone(aluno.getTelefone());
        alunoExistente.setEmail(aluno.getEmail());
        alunoExistente.setAtivo(aluno.getAtivo());
        alunoExistente.setEndereco(aluno.getEndereco());

        return alunoRepository.save(alunoExistente);
    }

    public void excluir(Long id) {

        Aluno aluno = buscarPorId(id);

        if (matriculaRepository.existsByAlunoId(id)) {
            throw new EntidadeConflitoException(
                    "Não é possível excluir este aluno porque existem matrículas associadas. "
                            + "Torne o aluno inativo para preservar o histórico."
            );
        }

        alunoRepository.delete(aluno);
    }

}