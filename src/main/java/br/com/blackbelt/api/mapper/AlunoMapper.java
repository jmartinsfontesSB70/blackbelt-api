package br.com.blackbelt.api.mapper;

import br.com.blackbelt.api.dto.AlunoRequest;
import br.com.blackbelt.api.dto.AlunoResponse;
import br.com.blackbelt.api.dto.EnderecoResponse;
import br.com.blackbelt.domain.model.Aluno;
import br.com.blackbelt.domain.model.Cidade;
import br.com.blackbelt.domain.model.Endereco;
import br.com.blackbelt.domain.repository.CidadeRepository;
import br.com.blackbelt.exception.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Component;

@Component
public class AlunoMapper {

    private final CidadeRepository cidadeRepository;

    public AlunoMapper(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

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
        response.setPossuiFoto(aluno.getFotoKey() != null);

        if (aluno.getEndereco() != null) {

            Endereco endereco = aluno.getEndereco();

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
