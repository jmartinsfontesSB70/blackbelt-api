package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.UsuarioAtualizacaoRequest;
import br.com.blackbelt.api.dto.UsuarioRequest;
import br.com.blackbelt.api.dto.UsuarioResponse;
import br.com.blackbelt.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequest request) {
        Usuario usuario = new Usuario();

        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setAtivo(request.getAtivo());

        return usuario;
    }

    public Usuario toEntity(UsuarioAtualizacaoRequest request) {
        Usuario usuario = new Usuario();

        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setAtivo(request.getAtivo());

        return usuario;
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();

        response.setId(usuario.getId());
        response.setUsername(usuario.getUsername());
        response.setEmail(usuario.getEmail());
        response.setAtivo(usuario.getAtivo());
        response.setPerfilId(usuario.getPerfil().getId());
        response.setPerfilNome(usuario.getPerfil().getNome());

        return response;
    }
}