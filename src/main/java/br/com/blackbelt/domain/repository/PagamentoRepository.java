package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findByCobrancaId(Long cobrancaId);

    List<Pagamento> findByCobrancaMatriculaAlunoId(Long alunoId);

    @Query("""
        SELECT COALESCE(SUM(p.valor), 0)
        FROM Pagamento p
        WHERE p.cobranca.id = :cobrancaId
    """)
    BigDecimal somarPagamentosPorCobranca(
            @Param("cobrancaId") Long cobrancaId
    );
}