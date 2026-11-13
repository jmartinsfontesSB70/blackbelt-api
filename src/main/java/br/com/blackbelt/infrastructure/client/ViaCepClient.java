package br.com.blackbelt.infrastructure.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ViaCepClient {

    private final RestClient restClient;

    public ViaCepClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://viacep.com.br/ws")
                .build();
    }

    public ViaCepResponse buscarCep(String cep) {

        return restClient.get()
                .uri("/{cep}/json/", cep)
                .retrieve()
                .body(ViaCepResponse.class);
    }

    public record ViaCepResponse(
            String cep,
            String logradouro,
            String complemento,
            String bairro,
            String localidade,
            String uf,
            String ibge,
            Boolean erro
    ) {}
}