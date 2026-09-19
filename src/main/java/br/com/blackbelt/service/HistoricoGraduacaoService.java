package br.com.blackbelt.service;

import br.com.blackbelt.api.dto.HistoricoGraduacaoRequest;
import br.com.blackbelt.api.dto.HistoricoGraduacaoResponse;
import br.com.blackbelt.api.mapper.HistoricoGraduacaoMapper;
import br.com.blackbelt.domain.model.Aluno;
import br.com.blackbelt.domain.model.Graduacao;
import br.com.blackbelt.domain.model.HistoricoGraduacao;
import br.com.blackbelt.domain.repository.AlunoRepository;
import br.com.blackbelt.domain.repository.GraduacaoRepository;
import br.com.blackbelt.domain.repository.HistoricoGraduacaoRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class HistoricoGraduacaoService {

    private final HistoricoGraduacaoRepository historicoRepository;
    private final AlunoRepository alunoRepository;
    private final GraduacaoRepository graduacaoRepository;
    private final HistoricoGraduacaoMapper mapper;

    public HistoricoGraduacaoService(
            HistoricoGraduacaoRepository historicoRepository,
            AlunoRepository alunoRepository,
            GraduacaoRepository graduacaoRepository,
            HistoricoGraduacaoMapper mapper) {

        this.historicoRepository = historicoRepository;
        this.alunoRepository = alunoRepository;
        this.graduacaoRepository = graduacaoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public HistoricoGraduacaoResponse salvar(
            HistoricoGraduacaoRequest request) {

        Aluno aluno = alunoRepository
                .findById(request.getAlunoId())
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Aluno não encontrado."
                        )
                );

        Graduacao graduacao = graduacaoRepository
                .findById(request.getGraduacaoId())
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Graduação não encontrada."
                        )
                );

        if (request.getData().isAfter(LocalDate.now())) {
            throw new EntidadeConflitoException(
                    "A data da graduação não pode ser futura."
            );
        }

        boolean utilizaGrau =
                graduacao.getModalidade().getUtilizaGrau();

        if (!utilizaGrau && request.getGrau() != 0) {
            throw new EntidadeConflitoException(
                    "A modalidade não utiliza graus. " +
                            "O grau deve ser 0."
            );
        }

        if (request.getGrau() > graduacao.getQuantidadeGraus()) {
            throw new EntidadeConflitoException(
                    "O grau informado ultrapassa o limite " +
                            "permitido para esta graduação."
            );
        }

        /*
         * Busca a graduação de maior ordem já registrada
         * para o aluno.
         *
         * A comparação é feita pela ordem da graduação,
         * e não pelo ID.
         */
        List<Graduacao> graduacoesDoAluno =
                historicoRepository
                        .buscarGraduacoesDoAlunoOrdenadasPorOrdem(
                                aluno.getId()
                        );

        if (!graduacoesDoAluno.isEmpty()) {

            Graduacao graduacaoAtual =
                    graduacoesDoAluno.get(0);

            /*
             * Não permite voltar para uma graduação anterior.
             */
            if (graduacao.getOrdem() < graduacaoAtual.getOrdem()) {
                throw new EntidadeConflitoException(
                        "Não é possível registrar uma graduação anterior " +
                                "à graduação atual do aluno."
                );
            }

            /*
             * Está avançando para uma nova graduação.
             *
             * A graduação atual precisa estar completamente concluída
             * antes de permitir o avanço.
             */
            if (graduacao.getOrdem() > graduacaoAtual.getOrdem()) {

                Integer grausAtuais =
                        historicoRepository
                                .somarGrausPorAlunoEGraduacao(
                                        aluno.getId(),
                                        graduacaoAtual.getId()
                                );

                if (grausAtuais < graduacaoAtual.getQuantidadeGraus()) {
                    throw new EntidadeConflitoException(
                            "A graduação atual ainda não atingiu " +
                                    "a quantidade máxima de graus permitida."
                    );
                }

                /*
                 * O primeiro registro da nova graduação
                 * obrigatoriamente deve ser grau 0.
                 */
                boolean primeiraGraduacao =
                        !historicoRepository
                                .existsByAlunoIdAndGraduacaoId(
                                        aluno.getId(),
                                        graduacao.getId()
                                );

                if (primeiraGraduacao && request.getGrau() != 0) {
                    throw new EntidadeConflitoException(
                            "O primeiro registro de uma nova graduação " +
                                    "deve ser com grau 0."
                    );
                }
            }
        }

        /*
         * Se o aluno ainda não possui nenhuma graduação registrada,
         * o primeiro registro também deve ser grau 0.
         */
        else {

            boolean primeiraGraduacao =
                    !historicoRepository
                            .existsByAlunoIdAndGraduacaoId(
                                    aluno.getId(),
                                    graduacao.getId()
                            );

            if (primeiraGraduacao && request.getGrau() != 0) {
                throw new EntidadeConflitoException(
                        "O primeiro registro de uma graduação " +
                                "deve ser com grau 0."
                );
            }
        }

        /*
         * Grau 0 só pode ser registrado uma vez para o mesmo
         * aluno e para a mesma graduação.
         */
        if (request.getGrau() == 0) {

            boolean grauZeroJaRegistrado =
                    historicoRepository
                            .existsByAlunoIdAndGraduacaoIdAndGrau(
                                    aluno.getId(),
                                    graduacao.getId(),
                                    0
                            );

            if (grauZeroJaRegistrado) {
                throw new EntidadeConflitoException(
                        "O aluno já possui o grau 0 registrado " +
                                "para esta graduação."
                );
            }
        }

        /*
         * Na mesma graduação, os graus podem ser registrados
         * em qualquer ordem.
         *
         * O que importa é que a soma acumulada não ultrapasse
         * a quantidade máxima de graus da graduação.
         */
        Integer grausCadastrados =
                historicoRepository.somarGrausPorAlunoEGraduacao(
                        aluno.getId(),
                        graduacao.getId()
                );

        int totalGraus =
                grausCadastrados + request.getGrau();

        if (totalGraus > graduacao.getQuantidadeGraus()) {
            throw new EntidadeConflitoException(
                    "A soma dos graus deste aluno para esta " +
                            "graduação ultrapassa o limite permitido."
            );
        }

        HistoricoGraduacao historico =
                new HistoricoGraduacao();

        historico.setAluno(aluno);
        historico.setGraduacao(graduacao);
        historico.setGrau(request.getGrau());
        historico.setData(request.getData());
        historico.setObservacao(request.getObservacao());

        return mapper.toResponse(
                historicoRepository.save(historico)
        );
    }

    @Transactional(readOnly = true)
    public List<HistoricoGraduacaoResponse> listarPorAluno(
            Long alunoId) {

        if (!alunoRepository.existsById(alunoId)) {
            throw new EntidadeNaoEncontradaException(
                    "Aluno não encontrado."
            );
        }

        return historicoRepository
                .findByAlunoIdOrderByDataDesc(alunoId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}