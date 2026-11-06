package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    Optional<Cidade> findByCodigoIbge(String codigoIbge);

    List<Cidade> findByUfIdOrderByNomeAsc(Long ufId);
}