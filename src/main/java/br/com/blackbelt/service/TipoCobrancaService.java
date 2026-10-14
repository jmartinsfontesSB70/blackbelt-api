package br.com.blackbelt.service;

import br.com.blackbelt.api.dto.TipoCobrancaRequest;
import br.com.blackbelt.api.dto.TipoCobrancaResponse;
import br.com.blackbelt.api.mapper.TipoCobrancaMapper;
import br.com.blackbelt.domain.model.TipoCobranca;
import br.com.blackbelt.domain.repository.CobrancaRepository;
import br.com.blackbelt.domain.repository.TipoCobrancaRepository;
import br.com.blackbelt.exception.EntidadeConflitoException;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoCobrancaService {

    private final TipoCobrancaRepository tipoCobrancaRepository;
    private final TipoCobrancaMapper tipoCobrancaMapper;
    private final CobrancaRepository cobrancaRepository;

    public TipoCobrancaService(
            TipoCobrancaRepository tipoCobrancaRepository,
            TipoCobrancaMapper tipoCobrancaMapper,
            CobrancaRepository cobrancaRepository
    ) {
        this.tipoCobrancaRepository = tipoCobrancaRepository;
        this.tipoCobrancaMapper = tipoCobrancaMapper;
        this.cobrancaRepository = cobrancaRepository;
    }

    @Transactional
    public TipoCobrancaResponse criar(
            TipoCobrancaRequest request
    ) {

        validarNomeDuplicado(request.getNome());

        TipoCobranca tipoCobranca = new TipoCobranca();

        tipoCobranca.setNome(request.getNome());
        tipoCobranca.setDescricao(request.getDescricao());
        tipoCobranca.setValorUnitario(request.getValorUnitario());
        tipoCobranca.setAtivo(request.getAtivo());

        TipoCobranca salvo =
                tipoCobrancaRepository.save(tipoCobranca);

        return tipoCobrancaMapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public TipoCobrancaResponse buscarPorId(Long id) {

        TipoCobranca tipoCobranca =
                buscarEntidade(id);

        return tipoCobrancaMapper.toResponse(tipoCobranca);
    }

    @Transactional(readOnly = true)
    public List<TipoCobrancaResponse> listar() {

        return tipoCobrancaRepository.findAll()
                .stream()
                .map(tipoCobrancaMapper::toResponse)
                .toList();
    }

    @Transactional
    public TipoCobrancaResponse atualizar(
            Long id,
            TipoCobrancaRequest request
    ) {

        TipoCobranca tipoCobranca =
                buscarEntidade(id);

        tipoCobrancaRepository
                .findByNomeIgnoreCaseAndIdNot(
                        request.getNome(),
                        id
                )
                .ifPresent(existente -> {
                    throw new EntidadeConflitoException(
                            "Já existe um tipo de cobrança com esse nome."
                    );
                });

        tipoCobranca.setNome(request.getNome());
        tipoCobranca.setDescricao(request.getDescricao());
        tipoCobranca.setValorUnitario(
                request.getValorUnitario()
        );
        tipoCobranca.setAtivo(request.getAtivo());

        TipoCobranca salvo =
                tipoCobrancaRepository.save(tipoCobranca);

        return tipoCobrancaMapper.toResponse(salvo);
    }

    @Transactional
    public void excluir(Long id) {

        TipoCobranca tipoCobranca =
                buscarEntidade(id);

        if (cobrancaRepository.existsByTipoCobrancaId(id)) {
            throw new EntidadeConflitoException(
                    "Não é possível excluir o tipo de cobrança porque existem cobranças associadas."
            );
        }

        tipoCobrancaRepository.delete(tipoCobranca);
    }

    private TipoCobranca buscarEntidade(Long id) {

        return tipoCobrancaRepository.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoEncontradaException(
                                "Tipo de cobrança não encontrado."
                        )
                );
    }

    private void validarNomeDuplicado(String nome) {

        tipoCobrancaRepository
                .findByNomeIgnoreCase(nome)
                .ifPresent(existente -> {
                    throw new EntidadeConflitoException(
                            "Já existe um tipo de cobrança com esse nome."
                    );
                });
    }
}