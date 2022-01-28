package br.com.zup.gerenciadorDePostagem.usuario.config;

import br.com.zup.gerenciadorDePostagem.config.security.jwt.exceptions.AcessoNegadoException;
import br.com.zup.gerenciadorDePostagem.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public List<MensagemDeErro> manipularErrosDeValidacao(MethodArgumentNotValidException exception) {
        List<MensagemDeErro> mensagens = new ArrayList<>();

        for (FieldError fieldError : exception.getFieldErrors()) {
            mensagens.add(new MensagemDeErro(fieldError.getDefaultMessage()));
        }
        return mensagens;
    }


    @ExceptionHandler(EmailJaCadastradoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public MensagemDeErro manipularExcecaoDeEmailCadastrado(EmailJaCadastradoException exception) {
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(NaoExistemPostagensCadastradasException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public MensagemDeErro manipularExcecaoDePostagensNaoCadastradas(NaoExistemPostagensCadastradasException exception) {
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(NaoExistemUsuariosCadastradosException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MensagemDeErro manipularExcecaoDeUsuarioNaoCadastrado(NaoExistemUsuariosCadastradosException exception) {
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(PostagemNaoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MensagemDeErro manipularExcecaoPostagemNaoEncontrada(PostagemNaoEncontradaException exception) {
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(UsuarioNaoAutorizadoException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public MensagemDeErro manipularExcecaoUsuarioNaoAutorizado(UsuarioNaoAutorizadoException exception) {
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(UsuarioNaoCadastradoException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public MensagemDeErro manipularExcecaoUsuarioNaoCadastrado(UsuarioNaoCadastradoException exception) {
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(AcessoNegadoException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public MensagemDeErro manipularExcecaoUsuarioNaoCadastrado(UsuarioNaoCadastradoException exception) {
        return new MensagemDeErro(exception.getMessage());
    }

}