package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Graduacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GraduacaoRepository
        extends JpaRepository<Graduacao, Long> {

    List<Graduacao> findByModalidadeIdOrderByOrdemAsc(Long modalidadeId);

    boolean existsByModalidadeIdAndNome(
            Long modalidadeId,
            String nome
    );

    boolean existsByModalidadeIdAndNomeAndIdNot(
            Long modalidadeId,
            String nome,
            Long id
    );
}