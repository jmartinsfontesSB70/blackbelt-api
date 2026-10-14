package br.com.blackbelt.service;

import br.com.blackbelt.domain.model.Aluno;
import br.com.blackbelt.domain.repository.AlunoRepository;
import br.com.blackbelt.domain.repository.MatriculaRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import br.com.blackbelt.service.storage.FotoStorage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final MatriculaRepository matriculaRepository;
    private final FotoStorage fotoStorage;

    public AlunoService(
            AlunoRepository alunoRepository,
            MatriculaRepository matriculaRepository,
            FotoStorage fotoStorage) {

        this.alunoRepository = alunoRepository;
        this.matriculaRepository = matriculaRepository;
        this.fotoStorage = fotoStorage;
    }

    public Page<Aluno> listar(
            Pageable pageable,
            String pesquisa) {

        if (pesquisa == null || pesquisa.isBlank()) {
            return alunoRepository.findAll(pageable);
        }

        return alunoRepository.findByNomeContainingIgnoreCase(
                pesquisa.trim(),
                pageable
        );
    }

    public Aluno cadastrar(Aluno aluno) {

        if (alunoRepository.existsByCpf(aluno.getCpf())) {
            throw new EntidadeConflitoException(
                    "Já existe um aluno cadastrado com este CPF.");
        }

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

        if (aluno.getFotoKey() != null) {
            try {
                fotoStorage.excluir(aluno.getFotoKey());
            } catch (IOException e) {
                throw new IllegalStateException(
                        "Não foi possível excluir a foto do aluno.",
                        e
                );
            }
        }

        alunoRepository.delete(aluno);
    }

    public Aluno salvarFoto(Long id, MultipartFile arquivo) {

        if (arquivo == null || arquivo.isEmpty()) {
            throw new EntidadeConflitoException(
                    "A foto é obrigatória."
            );
        }

        Aluno aluno = buscarPorId(id);

        try {

            if (aluno.getFotoKey() != null) {
                fotoStorage.excluir(aluno.getFotoKey());
            }

            String fotoKey = fotoStorage.salvar(
                    "alunos",
                    id,
                    arquivo
            );

            aluno.setFotoKey(fotoKey);

            return alunoRepository.save(aluno);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível salvar a foto do aluno.",
                    e
            );
        }
    }

    public byte[] buscarFoto(Long id) {

        Aluno aluno = buscarPorId(id);

        if (aluno.getFotoKey() == null) {
            throw new EntidadeNaoEncontradaException(
                    "O aluno não possui foto."
            );
        }

        try {
            return fotoStorage.buscar(aluno.getFotoKey());
        } catch (IOException e) {
            throw new EntidadeNaoEncontradaException(
                    "Foto do aluno não encontrada."
            );
        }
    }

    public void excluirFoto(Long id) {

        Aluno aluno = buscarPorId(id);

        if (aluno.getFotoKey() == null) {
            throw new EntidadeNaoEncontradaException(
                    "O aluno não possui foto."
            );
        }

        try {
            fotoStorage.excluir(aluno.getFotoKey());

            aluno.setFotoKey(null);

            alunoRepository.save(aluno);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível excluir a foto do aluno.",
                    e
            );
        }
    }
}