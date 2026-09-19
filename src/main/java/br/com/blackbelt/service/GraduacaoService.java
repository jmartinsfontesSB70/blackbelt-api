package br.com.blackbelt.service;

import br.com.blackbelt.api.dto.GraduacaoRequest;
import br.com.blackbelt.api.dto.GraduacaoResponse;
import br.com.blackbelt.api.mapper.GraduacaoMapper;
import br.com.blackbelt.domain.model.Graduacao;
import br.com.blackbelt.domain.model.Modalidade;
import br.com.blackbelt.domain.repository.GraduacaoRepository;
import br.com.blackbelt.domain.repository.HistoricoGraduacaoRepository;
import br.com.blackbelt.domain.repository.ModalidadeRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GraduacaoService {

    private final GraduacaoRepository graduacaoRepository;
    private final ModalidadeRepository modalidadeRepository;
    private final HistoricoGraduacaoRepository historicoGraduacaoRepository;
    private final GraduacaoMapper graduacaoMapper;

    public GraduacaoService(
            GraduacaoRepository graduacaoRepository,
            ModalidadeRepository modalidadeRepository,
            HistoricoGraduacaoRepository historicoGraduacaoRepository,
            GraduacaoMapper graduacaoMapper) {

        this.graduacaoRepository = graduacaoRepository;
        this.modalidadeRepository = modalidadeRepository;
        this.historicoGraduacaoRepository = historicoGraduacaoRepository;
        this.graduacaoMapper = graduacaoMapper;
    }

    @Transactional
    public GraduacaoResponse salvar(GraduacaoRequest request) {

        Modalidade modalidade = modalidadeRepository
                .findById(request.getModalidadeId())
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Modalidade não encontrada."
                        )
                );

        if (graduacaoRepository.existsByModalidadeIdAndNome(
                request.getModalidadeId(),
                request.getNome())) {

            throw new EntidadeConflitoException(
                    "Já existe uma graduação com esse nome " +
                            "nesta modalidade."
            );
        }

        Graduacao graduacao = new Graduacao();

        graduacao.setModalidade(modalidade);
        graduacao.setNome(request.getNome());
        graduacao.setOrdem(request.getOrdem());
        graduacao.setQuantidadeGraus(
                request.getQuantidadeGraus()
        );

        return graduacaoMapper.toResponse(
                graduacaoRepository.save(graduacao)
        );
    }

    @Transactional
    public GraduacaoResponse atualizar(
            Long id,
            GraduacaoRequest request) {

        Graduacao graduacao = graduacaoRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Graduação não encontrada."
                        )
                );

        Modalidade modalidade = modalidadeRepository
                .findById(request.getModalidadeId())
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Modalidade não encontrada."
                        )
                );

        if (graduacaoRepository
                .existsByModalidadeIdAndNomeAndIdNot(
                        request.getModalidadeId(),
                        request.getNome(),
                        id)) {

            throw new EntidadeConflitoException(
                    "Já existe uma graduação com esse nome " +
                            "nesta modalidade."
            );
        }

        boolean possuiHistorico =
                historicoGraduacaoRepository
                        .existsByGraduacaoId(id);

        if (possuiHistorico &&
                !graduacao.getModalidade().getId()
                        .equals(request.getModalidadeId())) {

            throw new EntidadeConflitoException(
                    "Não é possível alterar a modalidade " +
                            "de uma graduação que possui histórico."
            );
        }

        if (possuiHistorico) {

            Integer maiorGrau =
                    historicoGraduacaoRepository
                            .buscarMaiorGrauPorGraduacao(id);

            if (maiorGrau != null &&
                    request.getQuantidadeGraus() < maiorGrau) {

                throw new EntidadeConflitoException(
                        "A quantidade de graus não pode ser menor " +
                                "que o maior grau já registrado " +
                                "no histórico desta graduação."
                );
            }
        }

        graduacao.setModalidade(modalidade);
        graduacao.setNome(request.getNome());
        graduacao.setOrdem(request.getOrdem());
        graduacao.setQuantidadeGraus(
                request.getQuantidadeGraus()
        );

        return graduacaoMapper.toResponse(
                graduacaoRepository.save(graduacao)
        );
    }

    @Transactional
    public void excluir(Long id) {

        Graduacao graduacao = graduacaoRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Graduação não encontrada."
                        )
                );

        if (historicoGraduacaoRepository
                .existsByGraduacaoId(id)) {

            throw new EntidadeConflitoException(
                    "Não é possível excluir uma graduação " +
                            "que possui histórico."
            );
        }

        graduacaoRepository.delete(graduacao);
    }

    @Transactional(readOnly = true)
    public List<GraduacaoResponse> listarPorModalidade(
            Long modalidadeId) {

        if (!modalidadeRepository.existsById(modalidadeId)) {
            throw new EntidadeNaoEncontradaException(
                    "Modalidade não encontrada."
            );
        }

        return graduacaoRepository
                .findByModalidadeIdOrderByOrdemAsc(modalidadeId)
                .stream()
                .map(graduacaoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GraduacaoResponse buscarPorId(Long id) {

        Graduacao graduacao = graduacaoRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Graduação não encontrada."
                        )
                );

        return graduacaoMapper.toResponse(graduacao);
    }
}