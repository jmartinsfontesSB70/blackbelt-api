package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.PermissaoResponse;
import br.com.blackbelt.domain.model.Permissao;
import org.springframework.stereotype.Component;

@Component
public class PermissaoMapper {

    public PermissaoResponse toResponse(Permissao permissao) {

        return new PermissaoResponse(
                permissao.getId(),
                permissao.getNome()
        );
    }
}