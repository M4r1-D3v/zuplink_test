package br.com.zup.gerenciadorDePostagem.postagem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostagemServiceTest {

    @MockBean
    private PostagemRepository repository;

    @Autowired
    private  PostagemService service;


    @BeforeEach
    void setUp() {
    }

    @Test
    void salvarPostagem() {
    }

    @Test
    void exibirPostagens() {
    }

    @Test
    void atualizarPostagem() {
    }

    @Test
    void deletarPostagem() {
    }

}
