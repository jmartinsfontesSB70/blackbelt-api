package br.com.blackbelt.service;

import br.com.blackbelt.domain.model.Matricula;
import br.com.blackbelt.domain.model.Presenca;
import br.com.blackbelt.domain.repository.PresencaRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PresencaService {

    private final PresencaRepository presencaRepository;
    private final MatriculaService matriculaService;

    public PresencaService(PresencaRepository presencaRepository,
                           MatriculaService matriculaService) {
        this.presencaRepository = presencaRepository;
        this.matriculaService = matriculaService;
    }

    public Page<Presenca> listar(Pageable pageable) {
        return presencaRepository.findAll(pageable);
    }

    public Presenca buscarPorId(Long id) {
        return presencaRepository.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Presença de código " + id + " não encontrada."));
    }

    public Presenca cadastrar(Presenca presenca,
                               Long matriculaId) {

        if (matriculaId == null || matriculaId <= 0) {
            throw new EntidadeConflitoException(
                    "A matrícula da presença deve ser informada."
            );
        }

        validarDataPresenca(presenca.getData());

        Matricula matricula = matriculaService.buscarPorId(matriculaId);

        validarMatriculaAtiva(matricula);

        validarPresencaDuplicada(
                matriculaId,
                presenca.getData());

        presenca.setMatricula(matricula);

        return presencaRepository.save(presenca);

    }

    public Presenca atualizar(Long id,
                              Presenca presenca,
                              Long matriculaId) {

        if (matriculaId == null || matriculaId <= 0) {
            throw new EntidadeConflitoException(
                    "A matrícula da presença deve ser informada."
            );
        }

        validarDataPresenca(presenca.getData());

        Presenca presencaAtual = buscarPorId(id);

        Matricula matricula = matriculaService.buscarPorId(matriculaId);

        validarMatriculaAtiva(matricula);

        validarPresencaDuplicada(
                matriculaId,
                presenca.getData(),
                id);

        presencaAtual.setData(presenca.getData());
        presencaAtual.setObservacao(presenca.getObservacao());
        presencaAtual.setPresente(presenca.getPresente());

        presencaAtual.setMatricula(matricula);

        return presencaRepository.save(presencaAtual);
    }

    public void excluir(Long id) {

        Presenca presenca = buscarPorId(id);

        presencaRepository.delete(presenca);
    }

    private void validarMatriculaAtiva(Matricula matricula) {

        if (!Boolean.TRUE.equals(matricula.getAtiva())) {
            throw new EntidadeConflitoException(
                    "Não é possível registrar presença para uma matrícula inativa."
            );
        }
    }

    private void validarDataPresenca(LocalDate data) {

        if (data == null) {
            throw new EntidadeConflitoException(
                    "A data da presença deve ser informada."
            );
        }

        if (data.isAfter(LocalDate.now())) {
            throw new EntidadeConflitoException(
                    "A data da presença não pode ser futura."
            );
        }
    }

    private void validarPresencaDuplicada(Long matriculaId, LocalDate data) {

        boolean existe = presencaRepository
                .existsByMatriculaIdAndData(matriculaId, data);

        if (existe) {
            throw new EntidadeConflitoException(
                    "Já existe uma presença registrada para esta matrícula nesta data."
            );
        }
    }

    private void validarPresencaDuplicada(
            Long matriculaId,
            LocalDate data,
            Long presencaId) {

        boolean existe = presencaRepository
                .existsByMatriculaIdAndDataAndIdNot(
                        matriculaId,
                        data,
                        presencaId);

        if (existe) {
            throw new EntidadeConflitoException(
                    "Já existe uma presença registrada para esta matrícula nesta data."
            );
        }
    }
}
