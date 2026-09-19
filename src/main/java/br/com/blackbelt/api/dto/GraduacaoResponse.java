package br.com.blackbelt.api.dto;

public class GraduacaoResponse {

    private Long id;
    private Long modalidadeId;
    private String modalidadeNome;
    private String nome;
    private Integer ordem;
    private Integer quantidadeGraus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getModalidadeId() {
        return modalidadeId;
    }

    public void setModalidadeId(Long modalidadeId) {
        this.modalidadeId = modalidadeId;
    }

    public String getModalidadeNome() {
        return modalidadeNome;
    }

    public void setModalidadeNome(String modalidadeNome) {
        this.modalidadeNome = modalidadeNome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Integer getQuantidadeGraus() {
        return quantidadeGraus;
    }

    public void setQuantidadeGraus(Integer quantidadeGraus) {
        this.quantidadeGraus = quantidadeGraus;
    }
}