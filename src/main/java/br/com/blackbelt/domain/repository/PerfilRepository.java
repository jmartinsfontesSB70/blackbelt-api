package br.com.blackbelt.domain.repository;

import br.com.blackbelt.domain.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
}