package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.AcademiaRequest;
import br.com.blackbelt.api.dto.AcademiaResponse;
import br.com.blackbelt.api.dto.EnderecoResponse;
import br.com.blackbelt.domain.model.Academia;
import br.com.blackbelt.domain.model.Endereco;
import org.springframework.stereotype.Component;

@Component
public class AcademiaMapper {

    public Academia toEntity(AcademiaRequest request) {

        Academia academia = new Academia();

        academia.setNome(request.getNome());
        academia.setResponsavel(request.getResponsavel());
        academia.setCnpj(request.getCnpj());

        if (request.getEndereco() != null) {

            Endereco endereco = new Endereco();

            endereco.setRua(request.getEndereco().getRua());
            endereco.setNumero(request.getEndereco().getNumero());
            endereco.setBairro(request.getEndereco().getBairro());
            endereco.setCidade(request.getEndereco().getCidade());
            endereco.setEstado(request.getEndereco().getEstado());
            endereco.setCep(request.getEndereco().getCep());
            endereco.setPontoReferencia(
                    request.getEndereco().getPontoReferencia()
            );

            academia.setEndereco(endereco);
        }

        return academia;
    }

    public AcademiaResponse toResponse(Academia academia) {

        AcademiaResponse response = new AcademiaResponse();

        response.setId(academia.getId());
        response.setNome(academia.getNome());
        response.setResponsavel(academia.getResponsavel());
        response.setCnpj(academia.getCnpj());
        response.setPossuiLogo(academia.getLogoKey() != null);
        response.setDataCadastro(academia.getDataCadastro());
        response.setDataExpiracao(academia.getDataExpiracao());

        if (academia.getEndereco() != null) {

            EnderecoResponse endereco = new EnderecoResponse();

            endereco.setId(academia.getEndereco().getId());
            endereco.setRua(academia.getEndereco().getRua());
            endereco.setNumero(academia.getEndereco().getNumero());
            endereco.setBairro(academia.getEndereco().getBairro());
            endereco.setCidade(academia.getEndereco().getCidade());
            endereco.setEstado(academia.getEndereco().getEstado());
            endereco.setCep(academia.getEndereco().getCep());
            endereco.setPontoReferencia(
                    academia.getEndereco().getPontoReferencia()
            );

            response.setEndereco(endereco);
        }

        return response;
    }
}
