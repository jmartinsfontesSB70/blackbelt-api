package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Academia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademiaRepository extends JpaRepository<Academia, Long> {

}
