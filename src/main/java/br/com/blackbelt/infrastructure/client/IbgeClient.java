package br.com.blackbelt.infrastructure.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class IbgeClient {

    private final RestClient restClient;

    public IbgeClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://servicodados.ibge.gov.br/api/v1/localidades")
                .build();
    }

    public List<IbgeUfResponse> buscarUfs() {

        return restClient.get()
                .uri("/estados")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<IbgeCidadeResponse> buscarCidades(Long ufId) {

        return restClient.get()
                .uri("/estados/{ufId}/municipios", ufId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public record IbgeUfResponse(
            Long id,
            String sigla,
            String nome
    ) {}

    public record IbgeCidadeResponse(
            Long id,
            String nome
    ) {}
}