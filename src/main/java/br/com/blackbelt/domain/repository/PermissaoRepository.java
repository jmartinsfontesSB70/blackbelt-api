package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
}