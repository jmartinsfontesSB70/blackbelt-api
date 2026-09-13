package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    Page<Aluno> findByNomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );
}