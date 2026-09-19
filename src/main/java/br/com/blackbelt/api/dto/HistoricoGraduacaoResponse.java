package br.com.blackbelt.api.dto;

import java.time.LocalDate;

public class HistoricoGraduacaoResponse {

    private Long id;
    private Long alunoId;
    private String alunoNome;
    private Long graduacaoId;
    private String graduacaoNome;
    private Long modalidadeId;
    private String modalidadeNome;
    private Integer grau;
    private LocalDate data;
    private String observacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public void setAlunoNome(String alunoNome) {
        this.alunoNome = alunoNome;
    }

    public Long getGraduacaoId() {
        return graduacaoId;
    }

    public void setGraduacaoId(Long graduacaoId) {
        this.graduacaoId = graduacaoId;
    }

    public String getGraduacaoNome() {
        return graduacaoNome;
    }

    public void setGraduacaoNome(String graduacaoNome) {
        this.graduacaoNome = graduacaoNome;
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

    public Integer getGrau() {
        return grau;
    }

    public void setGrau(Integer grau) {
        this.grau = grau;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}