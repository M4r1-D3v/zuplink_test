package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static br.com.zup.gerenciadorDePostagem.enums.Area.BACKEND;
import static br.com.zup.gerenciadorDePostagem.enums.Tema.JAVA;
import static br.com.zup.gerenciadorDePostagem.enums.Tipo.DOCUMENTACAO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    private PostagemService service;


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
        assertEquals(postagem, response);
        verify(repository, times(1)).save(any(Postagem.class));

    }

    @Test
    public void testarExibirPostagensCaminhoPositivo() {
        when(repository.findAll()).thenReturn(List.of(postagem));

        List<Postagem> response = service.exibirPostagens();

        assertNotNull(response);
        assertEquals(Postagem.class, response.get(INT).getClass());
        assertEquals(1, response.size());

        assertEquals(ID_POSTAGEM, response.get(INT).getId());
        assertEquals(TITULO, response.get(INT).getTitulo());
        assertEquals(DESCRICAO, response.get(INT).getDescricao());
        assertEquals(LINK, response.get(INT).getLink());
        assertEquals(DOCUMENTACAO, response.get(INT).getTipo());
        assertEquals(JAVA, response.get(INT).getTema());
        assertEquals(BACKEND, response.get(INT).getAreaAtuacao());
        assertEquals(INT, response.get(INT).getLikes());
        assertEquals(INT, response.get(INT).getDeslikes());
        assertEquals(usuario, response.get(INT).getAutorPostagem());
        assertEquals(DATA_CADASTRO, response.get(INT).getDataDeCadastro());

        verify(repository, times(1)).findAll();

    }

    @Test
    void atualizarPostagem() {
    }

    @Test
    void deletarPostagem() {
    }


}
