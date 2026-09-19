package br.com.blackbelt.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "graduacoes")
@Getter
@Setter
@NoArgsConstructor
public class Graduacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modalidade_id", nullable = false)
    private Modalidade modalidade;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false)
    private Integer ordem;

    @Column(name = "quantidade_graus", nullable = false)
    private Integer quantidadeGraus;
}