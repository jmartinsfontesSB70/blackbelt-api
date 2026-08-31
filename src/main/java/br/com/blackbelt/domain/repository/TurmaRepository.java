package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaRepository extends JpaRepository<Turma, Long> {

    boolean existsByModalidadeId(Long modalidadeId);

    boolean existsByProfessorId(Long professorId);
}
