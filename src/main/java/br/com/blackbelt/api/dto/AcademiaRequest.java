package br.com.blackbelt.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class AcademiaRequest {

    @NotBlank(message = "O nome da academia é obrigatório.")
    private String nome;

    @NotBlank(message = "O responsável pela academia é obrigatório.")
    private String responsavel;

    private String cnpj;

    @Valid
    private EnderecoRequest endereco;

    public AcademiaRequest() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public EnderecoRequest getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoRequest endereco) {
        this.endereco = endereco;
    }
}
