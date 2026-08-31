package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.AlunoRequest;
import br.com.blackbelt.api.dto.AlunoResponse;
import br.com.blackbelt.api.dto.EnderecoResponse;
import br.com.blackbelt.domain.model.Aluno;
import br.com.blackbelt.domain.model.Endereco;
import org.springframework.stereotype.Component;

@Component
public class AlunoMapper {

    public Aluno toEntity(AlunoRequest request) {

        Aluno aluno = new Aluno();

        aluno.setNome(request.getNome());
        aluno.setDataNascimento(request.getDataNascimento());
        aluno.setCpf(request.getCpf());
        aluno.setTelefone(request.getTelefone());
        aluno.setEmail(request.getEmail());
        aluno.setAtivo(request.getAtivo());

        if (request.getEndereco() != null) {

            Endereco endereco = new Endereco();

            endereco.setRua(request.getEndereco().getRua());
            endereco.setNumero(request.getEndereco().getNumero());
            endereco.setBairro(request.getEndereco().getBairro());
            endereco.setCidade(request.getEndereco().getCidade());
            endereco.setEstado(request.getEndereco().getEstado());
            endereco.setCep(request.getEndereco().getCep());
            endereco.setPontoReferencia(request.getEndereco().getPontoReferencia());

            aluno.setEndereco(endereco);
        }

        return aluno;
    }

    public AlunoResponse toResponse(Aluno aluno) {

        AlunoResponse response = new AlunoResponse();

        response.setId(aluno.getId());
        response.setNome(aluno.getNome());
        response.setDataNascimento(aluno.getDataNascimento());
        response.setCpf(aluno.getCpf());
        response.setTelefone(aluno.getTelefone());
        response.setEmail(aluno.getEmail());
        response.setAtivo(aluno.getAtivo());

        if (aluno.getEndereco() != null) {

            EnderecoResponse endereco = new EnderecoResponse();

            endereco.setId(aluno.getEndereco().getId());
            endereco.setRua(aluno.getEndereco().getRua());
            endereco.setNumero(aluno.getEndereco().getNumero());
            endereco.setBairro(aluno.getEndereco().getBairro());
            endereco.setCidade(aluno.getEndereco().getCidade());
            endereco.setEstado(aluno.getEndereco().getEstado());
            endereco.setCep(aluno.getEndereco().getCep());
            endereco.setPontoReferencia(aluno.getEndereco().getPontoReferencia());

            response.setEndereco(endereco);
        }

        return response;
    }
}
