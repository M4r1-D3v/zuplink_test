package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static br.com.zup.gerenciadorDePostagem.enums.Area.*;
import static br.com.zup.gerenciadorDePostagem.enums.Tema.*;
import static br.com.zup.gerenciadorDePostagem.enums.Tipo.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PostagemServiceTest {

    public static final long ID_POSTAGEM = 1L;
    public static final String TITULO = "Titulo";
    public static final String DESCRICAO = "Descricao";
    public static final String LINK = "https://www.zup.com.br/";
    public static final String ID_USUARIO = "402880e67e97bc73017e97bdd9fa0000";
    public static final String NOME = "XABLAU";
    public static final String EMAIL = "xablau@zup.com.br";
    public static final String SENHA = "1234";
    public static final LocalDate DATA_CADASTRO = LocalDate.now();
    public static final int INT = 0;

    @MockBean
    private PostagemRepository repository;

    @Autowired
    private  PostagemService service;


    private Postagem postagem;
    private Usuario usuario;



    @BeforeEach
    void setUp() {
        usuario = new Usuario(ID_USUARIO, NOME, EMAIL, SENHA);
        postagem = new Postagem(ID_POSTAGEM, TITULO, DESCRICAO, LINK, DOCUMENTACAO, JAVA, BACKEND, INT, INT,
                usuario, DATA_CADASTRO);
    }

    @Test
    void testarSalvarPostagemCaminhoPositivo() {
        when(repository.save(any(Postagem.class))).thenReturn(postagem);

        Postagem response = service.salvarPostagem(postagem, usuario);

        assertNotNull(response);
        assertEquals(Postagem.class, response.getClass());
        assertEquals(postagem,response);
        verify(repository, times(1)).save(any(Postagem.class));

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
