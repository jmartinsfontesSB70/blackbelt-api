package br.com.blackbelt.api.dto;

import br.com.blackbelt.validation.CpfValido;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProfessorRequest {

    @NotBlank(message = "O nome do professor é obrigatório.")
    private String nome;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser anterior à data atual.")
    private LocalDate dataNascimento;

    @NotBlank(message = "CPF é obrigatório.")
    @CpfValido
    private String cpf;

    @NotBlank(message = "Telefone é obrigatório.")
    @Pattern(
            regexp = "\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}",
            message = "Telefone inválido."
    )
    private String telefone;

    @Email(message = "E-mail inválido.")
    @NotBlank(message = "E-mail é obrigatório.")
    private String email;

    @NotNull(message = "Data da contratação é obrigatória.")
    @PastOrPresent(message = "A data da contratação não pode ser futura.")
    private LocalDate dataContratacao;

    @NotNull(message = "Valor da hora aula é obrigatório.")
    @Positive(message = "Valor da hora aula deve ser maior que zero.")
    private BigDecimal valorHoraAula;

    @NotNull(message = "Situação do professor é obrigatória.")
    private Boolean ativo;

    @Valid
    @NotNull(message = "O endereço é obrigatório.")
    private EnderecoRequest endereco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(LocalDate dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public BigDecimal getValorHoraAula() {
        return valorHoraAula;
    }

    public void setValorHoraAula(BigDecimal valorHoraAula) {
        this.valorHoraAula = valorHoraAula;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public EnderecoRequest getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoRequest endereco) {
        this.endereco = endereco;
    }
}
