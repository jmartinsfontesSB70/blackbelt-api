package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.PerfilResponse;
import br.com.blackbelt.domain.model.Perfil;
import org.springframework.stereotype.Component;

@Component
public class PerfilMapper {

    private final PermissaoMapper permissaoMapper;

    public PerfilMapper(PermissaoMapper permissaoMapper) {
        this.permissaoMapper = permissaoMapper;
    }

    public PerfilResponse toResponse(Perfil perfil) {

        PerfilResponse response = new PerfilResponse();

        response.setId(perfil.getId());
        response.setNome(perfil.getNome());

        response.setPermissoes(
                perfil.getPermissoes()
                        .stream()
                        .map(permissaoMapper::toResponse)
                        .collect(java.util.stream.Collectors.toSet())
        );

        return response;
    }
}