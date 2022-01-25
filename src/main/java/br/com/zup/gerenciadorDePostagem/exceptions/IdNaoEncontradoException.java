package br.com.zup.gerenciadorDePostagem.exceptions;

public class IdNaoEncontradoException extends RuntimeException{
    public IdNaoEncontradoException(String message){
        super(message);
    }
}
