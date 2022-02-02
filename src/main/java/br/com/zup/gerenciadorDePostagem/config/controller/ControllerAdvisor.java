package br.com.zup.gerenciadorDePostagem.config.controller;

import br.com.zup.gerenciadorDePostagem.config.security.jwt.exceptions.AcessoNegadoException;
import br.com.zup.gerenciadorDePostagem.config.security.jwt.exceptions.TokenInvalidoException;
import br.com.zup.gerenciadorDePostagem.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    @ResponseStatus(HttpStatus.NOT_FOUND)
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
    public MensagemDeErro manipularExcecaoAcessoNegado(AcessoNegadoException exception) {
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(TokenInvalidoException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public MensagemDeErro manipularExcecaoTokenInvalido(TokenInvalidoException exception) {
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MensagemDeErro manipularExcecaoGeral(RuntimeException exception) {
        return new MensagemDeErro("Algo foi preenchido incorretamente, por favor tente novamente.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity manipularErroEnums(HttpMessageNotReadableException exception) {
        if (exception.getLocalizedMessage().contains("br.com.zup.gerenciadorDePostagem.enuns.Area")) {
            return ResponseEntity.status(422).build();
        }
        if (exception.getLocalizedMessage().contains("br.com.zup.gerenciadorDePostagem.enuns.Tema")) {
            return ResponseEntity.status(422).build();
        }
        if (exception.getLocalizedMessage().contains("br.com.zup.gerenciadorDePostagem.enuns.Tipo")) {
            return ResponseEntity.status(422).build();
        }

        return ResponseEntity.status(400).build();
    }

}