package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Graduacao;
import br.com.blackbelt.domain.model.HistoricoGraduacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoricoGraduacaoRepository
        extends JpaRepository<HistoricoGraduacao, Long> {

    List<HistoricoGraduacao> findByAlunoIdOrderByDataDesc(Long alunoId);

    boolean existsByGraduacaoId(Long graduacaoId);

    boolean existsByAlunoIdAndGraduacaoId(
            Long alunoId,
            Long graduacaoId
    );

    boolean existsByAlunoIdAndGraduacaoIdAndGrau(
            Long alunoId,
            Long graduacaoId,
            Integer grau
    );

    @Query("""
            SELECT COALESCE(SUM(h.grau), 0)
            FROM HistoricoGraduacao h
            WHERE h.aluno.id = :alunoId AND
                  h.graduacao.id = :graduacaoId
            """)
    Integer somarGrausPorAlunoEGraduacao(
            @Param("alunoId") Long alunoId,
            @Param("graduacaoId") Long graduacaoId
    );

    @Query("""
            SELECT MAX(h.grau)
            FROM HistoricoGraduacao h
            WHERE h.graduacao.id = :graduacaoId
            """)
    Integer buscarMaiorGrauPorGraduacao(
            @Param("graduacaoId") Long graduacaoId
    );

    @Query("""
            SELECT DISTINCT h.graduacao
            FROM HistoricoGraduacao h
            WHERE h.aluno.id = :alunoId
            ORDER BY h.graduacao.ordem DESC
            """)
    List<Graduacao> buscarGraduacoesDoAlunoOrdenadasPorOrdem(
            @Param("alunoId") Long alunoId
    );
}