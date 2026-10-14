package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Cobranca;
import br.com.blackbelt.domain.model.StatusCobranca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;

public interface CobrancaRepository
        extends JpaRepository<Cobranca, Long> {

    List<Cobranca> findByMatriculaIdAndCompetenciaAndTipoCobrancaId(
            Long matriculaId,
            YearMonth competencia,
            Long tipoCobrancaId
    );

    Page<Cobranca>
    findByMatriculaAlunoNomeContainingIgnoreCaseOrDescricaoContainingIgnoreCase(
            String nomeAluno,
            String descricao,
            Pageable pageable
    );

    List<Cobranca> findByMatriculaId(Long matriculaId);

    List<Cobranca> findByStatus(StatusCobranca status);

    List<Cobranca> findByMatriculaAlunoId(Long alunoId);

    boolean existsByTipoCobrancaId(Long tipoCobrancaId);
}