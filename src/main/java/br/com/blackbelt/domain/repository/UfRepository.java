package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Uf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UfRepository extends JpaRepository<Uf, Long> {

    Optional<Uf> findBySigla(String sigla);
}