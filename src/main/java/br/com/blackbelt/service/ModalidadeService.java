package br.com.blackbelt.service;

import br.com.blackbelt.domain.model.Modalidade;
import br.com.blackbelt.domain.repository.ModalidadeRepository;
import br.com.blackbelt.domain.repository.TurmaRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ModalidadeService {

    private final ModalidadeRepository modalidadeRepository;
    private final TurmaRepository turmaRepository;

    public ModalidadeService(
            ModalidadeRepository modalidadeRepository,
            TurmaRepository turmaRepository) {

        this.modalidadeRepository = modalidadeRepository;
        this.turmaRepository = turmaRepository;
    }

    public Page<Modalidade> listar(Pageable pageable) {
        return modalidadeRepository.findAll(pageable);
    }

    public Modalidade buscarPorId(Long id) {
        return modalidadeRepository.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Modalidade de código " + id + " não encontrada."));
    }

    public Modalidade cadastrar(Modalidade modalidade) {

        if (modalidadeRepository.existsByNomeIgnoreCase(modalidade.getNome())) {
            throw new EntidadeConflitoException(
                    "Já existe uma modalidade cadastrada com este nome.");
        }

        return modalidadeRepository.save(modalidade);
    }

    public Modalidade atualizar(Long id, Modalidade modalidade) {

        Modalidade modalidadeAtual = buscarPorId(id);

        if (modalidadeRepository.existsByNomeIgnoreCaseAndIdNot(
                modalidade.getNome(), id)) {

            throw new EntidadeConflitoException(
                    "Já existe uma modalidade cadastrada com este nome.");
        }

        modalidadeAtual.setNome(modalidade.getNome());
        modalidadeAtual.setDescricao(modalidade.getDescricao());
        modalidadeAtual.setAtiva(modalidade.getAtiva());

        return modalidadeRepository.save(modalidadeAtual);
    }

    public void excluir(Long id) {

        Modalidade modalidade = buscarPorId(id);

        if (turmaRepository.existsByModalidadeId(id)) {
            throw new EntidadeConflitoException(
                    "Não é possível excluir a modalidade porque ela está vinculada a uma ou mais turmas.");
        }

        modalidadeRepository.delete(modalidade);
    }
}
