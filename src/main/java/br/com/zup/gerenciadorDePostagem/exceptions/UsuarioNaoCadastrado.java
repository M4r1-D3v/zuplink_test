package br.com.zup.gerenciadorDePostagem.exceptions;

public class UsuarioNaoCadastrado extends RuntimeException{
    public UsuarioNaoCadastrado(String message){
        super(message);
    }

}
