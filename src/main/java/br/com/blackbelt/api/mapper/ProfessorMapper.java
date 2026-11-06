package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.EnderecoResponse;
import br.com.blackbelt.api.dto.ProfessorRequest;
import br.com.blackbelt.api.dto.ProfessorResponse;
import br.com.blackbelt.domain.model.Cidade;
import br.com.blackbelt.domain.model.Endereco;
import br.com.blackbelt.domain.model.Professor;
import br.com.blackbelt.domain.repository.CidadeRepository;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMapper {

    private final CidadeRepository cidadeRepository;

    public ProfessorMapper(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

    public Professor toEntity(ProfessorRequest request) {

        Professor professor = new Professor();

        professor.setNome(request.getNome());
        professor.setDataNascimento(request.getDataNascimento());
        professor.setCpf(request.getCpf());
        professor.setTelefone(request.getTelefone());
        professor.setEmail(request.getEmail());
        professor.setDataContratacao(request.getDataContratacao());
        professor.setValorHoraAula(request.getValorHoraAula());
        professor.setAtivo(request.getAtivo());

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

            professor.setEndereco(endereco);
        }

        return professor;
    }

    public ProfessorResponse toResponse(Professor professor) {

        ProfessorResponse response = new ProfessorResponse();

        response.setId(professor.getId());
        response.setNome(professor.getNome());
        response.setDataNascimento(professor.getDataNascimento());
        response.setCpf(professor.getCpf());
        response.setTelefone(professor.getTelefone());
        response.setEmail(professor.getEmail());
        response.setDataContratacao(professor.getDataContratacao());
        response.setValorHoraAula(professor.getValorHoraAula());
        response.setAtivo(professor.getAtivo());
        response.setPossuiFoto(professor.getFotoKey() != null);

        if (professor.getEndereco() != null) {

            Endereco endereco = professor.getEndereco();

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