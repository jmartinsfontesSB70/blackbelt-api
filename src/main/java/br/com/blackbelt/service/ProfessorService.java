package br.com.blackbelt.service;

import br.com.blackbelt.domain.model.Professor;
import br.com.blackbelt.domain.repository.ProfessorRepository;
import br.com.blackbelt.domain.repository.TurmaRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.blackbelt.service.storage.FotoStorage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final TurmaRepository turmaRepository;
    private final FotoStorage fotoStorage;

    public ProfessorService(
            ProfessorRepository professorRepository,
            TurmaRepository turmaRepository,
            FotoStorage fotoStorage) {

        this.professorRepository = professorRepository;
        this.turmaRepository = turmaRepository;
        this.fotoStorage = fotoStorage;
    }

    public Page<Professor> listar(
            Pageable pageable,
            String pesquisa) {

        if (pesquisa == null || pesquisa.isBlank()) {
            return professorRepository.findAll(pageable);
        }

        return professorRepository.findByNomeContainingIgnoreCase(
                pesquisa.trim(),
                pageable
        );
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

        if (professor.getFotoKey() != null) {

            try {

                fotoStorage.excluir(professor.getFotoKey());

            } catch (IOException e) {

                throw new IllegalStateException(
                        "Não foi possível excluir a foto do professor.",
                        e
                );
            }
        }

        professorRepository.delete(professor);
    }

    public Professor salvarFoto(Long id, MultipartFile arquivo) {

        Professor professor = buscarPorId(id);

        try {

            if (professor.getFotoKey() != null) {
                fotoStorage.excluir(professor.getFotoKey());
            }

            String fotoKey = fotoStorage.salvar(
                    "professores",
                    id,
                    arquivo
            );

            professor.setFotoKey(fotoKey);

            return professorRepository.save(professor);

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Não foi possível salvar a foto do professor.",
                    e
            );
        }
    }

    public byte[] buscarFoto(Long id) {

        Professor professor = buscarPorId(id);

        if (professor.getFotoKey() == null) {
            throw new EntidadeNaoEncontradaException(
                    "O professor não possui foto."
            );
        }

        try {

            return fotoStorage.buscar(professor.getFotoKey());

        } catch (IOException e) {

            throw new EntidadeNaoEncontradaException(
                    "Foto do professor não encontrada."
            );
        }
    }

    public void excluirFoto(Long id) {

        Professor professor = buscarPorId(id);

        if (professor.getFotoKey() == null) {
            throw new EntidadeNaoEncontradaException(
                    "O professor não possui foto."
            );
        }

        try {

            fotoStorage.excluir(professor.getFotoKey());

            professor.setFotoKey(null);

            professorRepository.save(professor);

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Não foi possível excluir a foto do professor.",
                    e
            );
        }
    }
}
