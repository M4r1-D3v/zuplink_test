package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.exceptions.NaoExistemPostagensCadastradasException;
import br.com.zup.gerenciadorDePostagem.exceptions.PostagemNaoEncontradaException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoAutorizadoException;
import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static br.com.zup.gerenciadorDePostagem.enums.Area.BACKEND;
import static br.com.zup.gerenciadorDePostagem.enums.Tema.JAVA;
import static br.com.zup.gerenciadorDePostagem.enums.Tipo.DOCUMENTACAO;
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
    public static final String NAO_EXISTEM_POSTAGENS_CADASTRADAS = "Não existem postagens cadastradas!";
    public static final String POSTAGEM_NAO_CADASTRADA = "Postagem não cadastrada";

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
    public void testarSalvarPostagemCaminhoPositivo() {
        when(repository.save(any(Postagem.class))).thenReturn(postagem);

        Postagem response = service.salvarPostagem(postagem, usuario);

        assertNotNull(response);
        assertEquals(Postagem.class, response.getClass());

        assertEquals(ID_POSTAGEM, response.getId());
        assertEquals(TITULO, response.getTitulo());
        assertEquals(DESCRICAO, response.getDescricao());
        assertEquals(LINK, response.getLink());
        assertEquals(DOCUMENTACAO, response.getTipo());
        assertEquals(JAVA,response.getTema());
        assertEquals(BACKEND, response.getAreaAtuacao());
        assertEquals(INT,response.getLikes());
        assertEquals(INT, response.getDeslikes());
        assertEquals(usuario, response.getAutorPostagem());
        assertEquals(DATA_CADASTRO, response.getDataDeCadastro());

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
    public void testarExibirPostagensExceptionNaoExistemPostagensCadastradas() {
        when(repository.findAll()).thenReturn(List.of());

        RuntimeException exception = assertThrows(NaoExistemPostagensCadastradasException.class,
                ()->{service.exibirPostagens();});

        assertEquals(NaoExistemPostagensCadastradasException.class, exception.getClass());
        assertEquals(NAO_EXISTEM_POSTAGENS_CADASTRADAS, exception.getMessage());

    }

    @Test
    public void testarAtualizarPostagemCaminhoPositivo() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(postagem));
        when(repository.save(any(Postagem.class))).thenReturn(postagem);

        Postagem response = service.atualizarPostagem(postagem.getId(), postagem,usuario.getId());

        assertNotNull(response);
        assertEquals(Postagem.class,response.getClass());

        assertEquals(ID_POSTAGEM, response.getId());
        assertEquals(TITULO, response.getTitulo());
        assertEquals(DESCRICAO, response.getDescricao());
        assertEquals(LINK, response.getLink());
        assertEquals(DOCUMENTACAO, response.getTipo());
        assertEquals(JAVA, response.getTema());
        assertEquals(BACKEND, response.getAreaAtuacao());
        assertEquals(INT, response.getLikes());
        assertEquals(INT, response.getDeslikes());
        assertEquals(usuario, response.getAutorPostagem());
        assertEquals(DATA_CADASTRO, response.getDataDeCadastro());

        verify(repository, times(1)).save(any(Postagem.class));
        verify(repository, times(1)).findById(anyLong());

    }

    @Test
    public void testarAtualizarPostagemExceptionPostagemNaoEncontrada() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(PostagemNaoEncontradaException.class,
                ()->{service.atualizarPostagem(postagem.getId(), postagem,usuario.getId());});

        assertEquals(PostagemNaoEncontradaException.class, exception.getClass());
        assertEquals(POSTAGEM_NAO_CADASTRADA,exception.getMessage());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).save(any(Postagem.class));
    }

    @Test
    void testarDeletarPostagemCaminhoPositivo() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(postagem));
        doNothing().when(repository).deleteById(anyLong());

        service.deletarPostagem(postagem.getId(),usuario.getId());

        verify(repository,times(1)).findById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void testarDeletarPostagemExceptionPostagemNaoEncontrada() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(PostagemNaoEncontradaException.class,
                () -> {service.deletarPostagem(postagem.getId(),usuario.getId());});

        assertEquals(PostagemNaoEncontradaException.class, exception.getClass());
        assertEquals(POSTAGEM_NAO_CADASTRADA,exception.getMessage());

        verify(repository,times(1)).findById(anyLong());
        verify(repository, times(0)).deleteById(anyLong());
    }

}
