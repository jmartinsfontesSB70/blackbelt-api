package br.com.blackbelt.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "historico_graduacoes")
@Getter
@Setter
@NoArgsConstructor
public class HistoricoGraduacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "graduacao_id", nullable = false)
    private Graduacao graduacao;

    @Column(nullable = false)
    private Integer grau;

    @Column(nullable = false)
    private LocalDate data;

    @Column(length = 500)
    private String observacao;
}
