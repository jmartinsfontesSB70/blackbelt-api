package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.TipoCobranca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoCobrancaRepository
        extends JpaRepository<TipoCobranca, Long> {

    Optional<TipoCobranca> findByNomeIgnoreCase(String nome);

    Optional<TipoCobranca> findByNomeIgnoreCaseAndIdNot(
            String nome,
            Long id
    );
}