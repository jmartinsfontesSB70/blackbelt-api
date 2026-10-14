package br.com.blackbelt.service;

import br.com.blackbelt.api.dto.CobrancaRequest;
import br.com.blackbelt.api.dto.CobrancaResponse;
import br.com.blackbelt.api.dto.PagamentoRequest;
import br.com.blackbelt.api.dto.PagamentoResponse;
import br.com.blackbelt.api.mapper.CobrancaMapper;
import br.com.blackbelt.api.mapper.PagamentoMapper;
import br.com.blackbelt.domain.model.*;
import br.com.blackbelt.domain.repository.CobrancaRepository;
import br.com.blackbelt.domain.repository.MatriculaRepository;
import br.com.blackbelt.domain.repository.PagamentoRepository;
import br.com.blackbelt.domain.repository.TipoCobrancaRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CobrancaService {

    private final CobrancaRepository cobrancaRepository;
    private final MatriculaRepository matriculaRepository;
    private final PagamentoRepository pagamentoRepository;
    private final TipoCobrancaRepository tipoCobrancaRepository;
    private final CobrancaMapper cobrancaMapper;
    private final PagamentoMapper pagamentoMapper;

    public CobrancaService(
            CobrancaRepository cobrancaRepository,
            MatriculaRepository matriculaRepository,
            PagamentoRepository pagamentoRepository,
            TipoCobrancaRepository tipoCobrancaRepository,
            CobrancaMapper cobrancaMapper,
            PagamentoMapper pagamentoMapper
    ) {
        this.cobrancaRepository = cobrancaRepository;
        this.matriculaRepository = matriculaRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.tipoCobrancaRepository = tipoCobrancaRepository;
        this.cobrancaMapper = cobrancaMapper;
        this.pagamentoMapper = pagamentoMapper;
    }

    @Transactional
    public CobrancaResponse criar(CobrancaRequest request) {

        Matricula matricula = matriculaRepository.findById(
                request.getMatriculaId()
        ).orElseThrow(() ->
                new EntidadeNaoEncontradaException(
                        "Matrícula não encontrada."
                )
        );

        TipoCobranca tipoCobranca =
                tipoCobrancaRepository.findById(
                        request.getTipoCobrancaId()
                ).orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Tipo de cobrança não encontrado."
                        )
                );

        List<Cobranca> cobrancasExistentes =
                cobrancaRepository
                        .findByMatriculaIdAndCompetenciaAndTipoCobrancaId(
                                request.getMatriculaId(),
                                request.getCompetencia(),
                                request.getTipoCobrancaId()
                        );

        boolean existeCobrancaAtiva = cobrancasExistentes.stream()
                .anyMatch(cobranca ->
                        cobranca.getStatus() != StatusCobranca.CANCELADO
                );

        if (existeCobrancaAtiva) {
            throw new EntidadeConflitoException(
                    "Já existe uma cobrança para esta matrícula, "
                            + "competência e tipo de cobrança."
            );
        }

        BigDecimal valorUnitario =
                tipoCobranca.getValorUnitario();

        BigDecimal valorTotal =
                valorUnitario.multiply(
                        BigDecimal.valueOf(
                                request.getQuantidade()
                        )
                );

        Cobranca cobranca = new Cobranca();

        cobranca.setMatricula(matricula);
        cobranca.setTipoCobranca(tipoCobranca);
        cobranca.setDescricao(request.getDescricao());
        cobranca.setCompetencia(request.getCompetencia());
        cobranca.setDataVencimento(
                request.getDataVencimento()
        );
        cobranca.setQuantidade(
                request.getQuantidade()
        );
        cobranca.setValorUnitario(valorUnitario);
        cobranca.setValor(valorTotal);
        cobranca.setObservacao(request.getObservacao());
        cobranca.setStatus(StatusCobranca.PENDENTE);

        Cobranca salva =
                cobrancaRepository.save(cobranca);

        return cobrancaMapper.toResponse(salva);
    }

    @Transactional(readOnly = true)
    public CobrancaResponse buscarPorId(Long id) {

        Cobranca cobranca = cobrancaRepository.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Cobrança não encontrada."
                        )
                );

        return cobrancaMapper.toResponse(cobranca);
    }

    @Transactional(readOnly = true)
    public Page<Cobranca> listar(Pageable pageable, String pesquisa) {

        if (pesquisa == null || pesquisa.isBlank()) {
            return cobrancaRepository.findAll(pageable);
        }

        String texto = pesquisa.trim();

        return cobrancaRepository
                .findByMatriculaAlunoNomeContainingIgnoreCaseOrDescricaoContainingIgnoreCase(
                        texto,
                        texto,
                        pageable
                );
    }

    @Transactional(readOnly = true)
    public List<CobrancaResponse> listarPorMatricula(
            Long matriculaId
    ) {

        if (!matriculaRepository.existsById(matriculaId)) {
            throw new EntidadeNaoEncontradaException(
                    "Matrícula não encontrada."
            );
        }

        return cobrancaRepository
                .findByMatriculaId(matriculaId)
                .stream()
                .map(cobrancaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponse> listarPagamentos(
            Long cobrancaId
    ) {

        if (!cobrancaRepository.existsById(cobrancaId)) {
            throw new EntidadeNaoEncontradaException(
                    "Cobrança não encontrada."
            );
        }

        return pagamentoRepository
                .findByCobrancaId(cobrancaId)
                .stream()
                .map(pagamentoMapper::toResponse)
                .toList();
    }

    @Transactional
    public PagamentoResponse registrarPagamento(
            PagamentoRequest request
    ) {

        Cobranca cobranca = cobrancaRepository.findById(
                request.getCobrancaId()
        ).orElseThrow(() ->
                new EntidadeNaoEncontradaException(
                        "Cobrança não encontrada."
                )
        );

        validarCobrancaParaPagamento(cobranca);

        BigDecimal totalPago =
                pagamentoRepository
                        .somarPagamentosPorCobranca(
                                cobranca.getId()
                        );

        BigDecimal novoTotal =
                totalPago.add(request.getValor());

        if (novoTotal.compareTo(cobranca.getValor()) > 0) {
            throw new EntidadeConflitoException(
                    "O valor do pagamento excede o valor da cobrança."
            );
        }

        Pagamento pagamento = new Pagamento();

        pagamento.setCobranca(cobranca);
        pagamento.setDataPagamento(
                request.getDataPagamento()
        );
        pagamento.setValor(request.getValor());
        pagamento.setFormaPagamento(
                request.getFormaPagamento()
        );
        pagamento.setObservacao(
                request.getObservacao()
        );

        Pagamento salvo =
                pagamentoRepository.save(pagamento);

        atualizarStatusCobranca(
                cobranca,
                novoTotal
        );

        cobrancaRepository.save(cobranca);

        return pagamentoMapper.toResponse(salvo);
    }

    @Transactional
    public void excluirPagamento(
            Long cobrancaId,
            Long pagamentoId
    ) {

        Cobranca cobranca = cobrancaRepository.findById(cobrancaId)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Cobrança não encontrada."
                        )
                );

        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Pagamento não encontrado."
                        )
                );

        if (!pagamento.getCobranca().getId().equals(cobranca.getId())) {
            throw new EntidadeConflitoException(
                    "O pagamento não pertence à cobrança informada."
            );
        }

        pagamentoRepository.delete(pagamento);

        BigDecimal totalPago =
                pagamentoRepository.somarPagamentosPorCobranca(
                        cobranca.getId()
                );

        if (totalPago.compareTo(BigDecimal.ZERO) == 0) {
            cobranca.setStatus(StatusCobranca.PENDENTE);
        } else {
            atualizarStatusCobranca(
                    cobranca,
                    totalPago
            );
        }

        cobrancaRepository.save(cobranca);
    }

    private void validarCobrancaParaPagamento(
            Cobranca cobranca
    ) {

        if (cobranca.getStatus() == StatusCobranca.PAGO) {
            throw new EntidadeConflitoException(
                    "A cobrança já está paga."
            );
        }

        if (cobranca.getStatus() == StatusCobranca.CANCELADO) {
            throw new EntidadeConflitoException(
                    "Não é possível registrar pagamento de uma cobrança cancelada."
            );
        }
    }

    private void atualizarStatusCobranca(
            Cobranca cobranca,
            BigDecimal totalPago
    ) {

        if (totalPago.compareTo(cobranca.getValor()) == 0) {
            cobranca.setStatus(StatusCobranca.PAGO);
        } else {
            cobranca.setStatus(StatusCobranca.PARCIAL);
        }
    }

    @Transactional
    public CobrancaResponse cancelar(Long cobrancaId) {

        Cobranca cobranca = cobrancaRepository.findById(cobrancaId)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Cobrança não encontrada."
                        )
                );

        if (cobranca.getStatus() == StatusCobranca.PAGO) {
            throw new EntidadeConflitoException(
                    "Não é possível cancelar uma cobrança já paga."
            );
        }

        if (cobranca.getStatus() == StatusCobranca.CANCELADO) {
            throw new EntidadeConflitoException(
                    "A cobrança já está cancelada."
            );
        }

        List<Pagamento> pagamentos =
                pagamentoRepository.findByCobrancaId(cobrancaId);

        if (!pagamentos.isEmpty()) {
            throw new EntidadeConflitoException(
                    "Não é possível cancelar uma cobrança que possui pagamentos."
            );
        }

        cobranca.setStatus(StatusCobranca.CANCELADO);
        cobranca.setDataCancelamento(LocalDate.now());

        Cobranca salva =
                cobrancaRepository.save(cobranca);

        return cobrancaMapper.toResponse(salva);
    }

    @Transactional
    public void excluir(Long cobrancaId) {

        Cobranca cobranca = cobrancaRepository.findById(cobrancaId)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Cobrança não encontrada."
                        )
                );

        List<Pagamento> pagamentos =
                pagamentoRepository.findByCobrancaId(cobrancaId);

        if (!pagamentos.isEmpty()) {
            throw new EntidadeConflitoException(
                    "Não é possível excluir uma cobrança que possui pagamentos."
            );
        }

        cobrancaRepository.delete(cobranca);
    }
}