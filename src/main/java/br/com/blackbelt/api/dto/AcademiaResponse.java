
package br.com.blackbelt.api.dto;

import java.time.LocalDate;

public class AcademiaResponse {

    private Long id;
    private String nome;
    private String responsavel;
    private String cnpj;
    private LocalDate dataCadastro;
    private LocalDate dataExpiracao;
    private EnderecoResponse endereco;
    private boolean possuiLogo;

    public AcademiaResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public EnderecoResponse getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoResponse endereco) {
        this.endereco = endereco;
    }

    public boolean isPossuiLogo() {
        return possuiLogo;
    }

    public void setPossuiLogo(boolean possuiLogo) {
        this.possuiLogo = possuiLogo;
    }
}
