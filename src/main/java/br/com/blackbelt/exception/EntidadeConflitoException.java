package br.com.blackbelt.exception;

public class EntidadeConflitoException extends RuntimeException {

    public EntidadeConflitoException(String mensagem) {
        super(mensagem);
    }

}