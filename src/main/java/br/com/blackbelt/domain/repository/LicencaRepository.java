package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Licenca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface LicencaRepository
        extends JpaRepository<Licenca, Long> {

    Optional<Licenca> findByTokenHash(String tokenHash);

    boolean existsByTokenHash(String tokenHash);

    Optional<Licenca>
    findFirstByDataInicioLessThanEqualAndDataExpiracaoAfter(
            LocalDate dataInicio,
            LocalDate dataExpiracao
    );

    Optional<Licenca>
    findTopByDataExpiracaoAfterOrderByDataExpiracaoDesc(
            LocalDate data
    );
}