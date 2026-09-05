package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    boolean existsByAlunoId(Long alunoId);

    boolean existsByAlunoIdAndTurmaIdAndAtivaTrue(Long alunoId, Long turmaId);

    boolean existsByAlunoIdAndTurmaIdAndAtivaTrueAndIdNot(
            Long alunoId,
            Long turmaId,
            Long id);

    boolean existsByTurmaId(Long turmaId);

   List<Matricula> findByTurmaIdAndAtivaTrueAndDataMatriculaLessThanEqual(
            Long turmaId,
            LocalDate data);

}
