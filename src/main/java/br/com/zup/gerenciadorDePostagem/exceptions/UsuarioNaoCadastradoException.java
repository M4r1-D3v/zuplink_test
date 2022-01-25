package br.com.zup.gerenciadorDePostagem.exceptions;

public class UsuarioNaoCadastradoException extends RuntimeException{

    public UsuarioNaoCadastradoException(String message){
        super(message);
    }

}
