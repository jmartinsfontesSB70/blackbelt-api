package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.AcademiaRequest;
import br.com.blackbelt.api.dto.AcademiaResponse;
import br.com.blackbelt.api.dto.EnderecoResponse;
import br.com.blackbelt.domain.model.Academia;
import br.com.blackbelt.domain.model.Cidade;
import br.com.blackbelt.domain.model.Endereco;
import br.com.blackbelt.domain.repository.CidadeRepository;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Component;

@Component
public class AcademiaMapper {

    private final CidadeRepository cidadeRepository;

    public AcademiaMapper(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

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
            endereco.setCep(request.getEndereco().getCep());
            endereco.setPontoReferencia(
                    request.getEndereco().getPontoReferencia()
            );

            Long cidadeId = request.getEndereco().getCidadeId();

            if (cidadeId == null) {
                throw new IllegalArgumentException(
                        "Cidade é obrigatória."
                );
            }

            Cidade cidade = cidadeRepository.findById(cidadeId)
                    .orElseThrow(() ->
                            new EntidadeNaoEncontradaException(
                                    "Cidade de código " + cidadeId
                                            + " não encontrada."
                            )
                    );

            endereco.setCidade(cidade);

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

            Endereco endereco = academia.getEndereco();

            EnderecoResponse enderecoResponse = new EnderecoResponse();

            enderecoResponse.setId(endereco.getId());
            enderecoResponse.setRua(endereco.getRua());
            enderecoResponse.setNumero(endereco.getNumero());
            enderecoResponse.setBairro(endereco.getBairro());
            enderecoResponse.setCep(endereco.getCep());
            enderecoResponse.setPontoReferencia(
                    endereco.getPontoReferencia()
            );

            if (endereco.getCidade() != null) {

                Cidade cidade = endereco.getCidade();

                enderecoResponse.setCidadeId(cidade.getId());
                enderecoResponse.setCidade(cidade.getNome());
                enderecoResponse.setUf(cidade.getUf().getSigla());
            }

            response.setEndereco(enderecoResponse);
        }

        return response;
    }
}
