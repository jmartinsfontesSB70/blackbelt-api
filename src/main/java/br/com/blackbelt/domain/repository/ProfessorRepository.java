package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    boolean existsByCpf(String cpf);

    Page<Professor> findByNomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );

}