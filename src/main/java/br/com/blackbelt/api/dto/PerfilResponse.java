package br.com.blackbelt.api.dto;

import java.util.Set;

public class PerfilResponse {

    private Long id;
    private String nome;
    private Set<PermissaoResponse> permissoes;

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

    public Set<PermissaoResponse> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(Set<PermissaoResponse> permissoes) {
        this.permissoes = permissoes;
    }
}