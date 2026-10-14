package br.com.blackbelt.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "licencas",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_licencas_token_hash",
                        columnNames = "token_hash"
                )
        }
)
public class Licenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "duracao_dias", nullable = false)
    private int duracaoDias;

    @Column(name = "data_geracao", nullable = false, updatable = false)
    private LocalDate dataGeracao;

    @Column(name = "data_ativacao")
    private LocalDate dataAtivacao;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_expiracao")
    private LocalDate dataExpiracao;

    public Licenca() {
    }

    public Long getId() {
        return id;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public int getDuracaoDias() {
        return duracaoDias;
    }

    public void setDuracaoDias(int duracaoDias) {
        this.duracaoDias = duracaoDias;
    }

    public LocalDate getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDate dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public LocalDate getDataAtivacao() {
        return dataAtivacao;
    }

    public void setDataAtivacao(LocalDate dataAtivacao) {
        this.dataAtivacao = dataAtivacao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }
}