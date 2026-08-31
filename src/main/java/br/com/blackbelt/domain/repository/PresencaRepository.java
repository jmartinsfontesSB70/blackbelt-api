package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PresencaRepository extends JpaRepository<Presenca, Long> {

    boolean existsByMatriculaIdAndData(
            Long matriculaId,
            LocalDate data);

    boolean existsByMatriculaIdAndDataAndIdNot(
            Long matriculaId,
            LocalDate data,
            Long id);

    boolean existsByMatriculaId(Long matriculaId);
}