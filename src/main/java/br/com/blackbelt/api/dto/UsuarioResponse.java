package br.com.blackbelt.api.dto;

public class UsuarioResponse {

    private Long id;
    private String username;
    private Boolean ativo;
    private Long perfilId;
    private String perfilNome;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public Long getPerfilId() {
        return perfilId;
    }

    public String getPerfilNome() {
        return perfilNome;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setPerfilId(Long perfilId) {
        this.perfilId = perfilId;
    }

    public void setPerfilNome(String perfilNome) {
        this.perfilNome = perfilNome;
    }
}