package br.com.blackbelt.api.dto;

public record EnderecoCepResponse(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        Long cidadeId,
        String cidade,
        String uf
) {}
