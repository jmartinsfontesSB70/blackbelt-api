package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.EnderecoResponse;
import br.com.blackbelt.api.dto.ProfessorRequest;
import br.com.blackbelt.api.dto.ProfessorResponse;
import br.com.blackbelt.domain.model.Endereco;
import br.com.blackbelt.domain.model.Professor;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMapper {

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
            endereco.setCidade(request.getEndereco().getCidade());
            endereco.setEstado(request.getEndereco().getEstado());
            endereco.setCep(request.getEndereco().getCep());
            endereco.setPontoReferencia(request.getEndereco().getPontoReferencia());

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

        if (professor.getEndereco() != null) {

            EnderecoResponse endereco = new EnderecoResponse();

            endereco.setId(professor.getEndereco().getId());
            endereco.setRua(professor.getEndereco().getRua());
            endereco.setNumero(professor.getEndereco().getNumero());
            endereco.setBairro(professor.getEndereco().getBairro());
            endereco.setCidade(professor.getEndereco().getCidade());
            endereco.setEstado(professor.getEndereco().getEstado());
            endereco.setCep(professor.getEndereco().getCep());
            endereco.setPontoReferencia(professor.getEndereco().getPontoReferencia());

            response.setEndereco(endereco);
        }

        return response;
    }
}
