package br.com.zup.gerenciadorDePostagem.exceptions;

public class UsuarioNaoAutorizadoException extends RuntimeException {

    public UsuarioNaoAutorizadoException(String message) {
        super(message);
    }

}
