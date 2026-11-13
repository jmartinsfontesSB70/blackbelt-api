package br.com.blackbelt.service;

import br.com.blackbelt.api.dto.EnderecoCepResponse;
import br.com.blackbelt.domain.model.Cidade;
import br.com.blackbelt.domain.repository.CidadeRepository;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import br.com.blackbelt.infrastructure.client.ViaCepClient;
import br.com.blackbelt.infrastructure.client.ViaCepClient.ViaCepResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class ViaCepService {

    private final ViaCepClient viaCepClient;
    private final CidadeRepository cidadeRepository;

    public ViaCepService(
            ViaCepClient viaCepClient,
            CidadeRepository cidadeRepository) {

        this.viaCepClient = viaCepClient;
        this.cidadeRepository = cidadeRepository;
    }

    public EnderecoCepResponse buscarCep(String cep) {

        if (cep == null || !cep.replaceAll("\\D", "").matches("\\d{8}")) {
            throw new IllegalArgumentException(
                    "O CEP deve conter exatamente 8 dígitos."
            );
        }

        String cepLimpo = cep.replaceAll("\\D", "");

        ViaCepResponse response;

        try {
            response = viaCepClient.buscarCep(cepLimpo);
        } catch (RestClientException e) {
            throw new IllegalStateException(
                    "Não foi possível consultar o serviço ViaCEP.",
                    e
            );
        }

        if (response == null || Boolean.TRUE.equals(response.erro())) {
            throw new EntidadeNaoEncontradaException(
                    "CEP não encontrado."
            );
        }

        Cidade cidade = cidadeRepository
                .findByCodigoIbge(response.ibge())
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Cidade não encontrada na base de dados."
                ));

        return new EnderecoCepResponse(
                response.cep(),
                response.logradouro(),
                response.complemento(),
                response.bairro(),
                cidade.getId(),
                cidade.getNome(),
                cidade.getUf().getSigla()
        );
    }
}