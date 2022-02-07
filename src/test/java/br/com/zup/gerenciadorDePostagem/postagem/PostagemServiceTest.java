package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.curtidas.Like;
import br.com.zup.gerenciadorDePostagem.curtidas.LikeRepository;
import br.com.zup.gerenciadorDePostagem.exceptions.NaoExistemPostagensCadastradasException;
import br.com.zup.gerenciadorDePostagem.exceptions.PostagemNaoEncontradaException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoAutorizadoException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoCadastradoException;
import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import br.com.zup.gerenciadorDePostagem.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.zup.gerenciadorDePostagem.enums.Area.BACKEND;
import static br.com.zup.gerenciadorDePostagem.enums.Tema.JAVA;
import static br.com.zup.gerenciadorDePostagem.enums.Tipo.DOCUMENTACAO;
import static java.util.Optional.*;
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
    public static final String POSTAGEM_NAO_CADASTRADA = "Postagem não encontrada";
    public static final String USUÁRIO_NAO_AUTORIZADO = "Usuário não autorizado";
    public static final long LONG = 1L;
    public static final String ID_TESTE = "402880e67e97bc73017e97bdd9fa0001";
    public static final String USUARIO_NAO_CADASTRADO = "Usuário não cadastrado";


    @MockBean
    private PostagemRepository repository;
    @MockBean
    private LikeRepository likeRepository;
    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PostagemService service;


    private Postagem postagem;
    private Usuario usuario;
    private Usuario usuarioTeste;
    private Map<String, String> filtro;
    private Like like;


    @BeforeEach
    void setUp() {
        usuario = new Usuario(ID_USUARIO, NOME, EMAIL, SENHA);
        postagem = new Postagem(ID_POSTAGEM, TITULO, DESCRICAO, LINK, DOCUMENTACAO, JAVA, BACKEND, INT,
                usuario, DATA_CADASTRO);
        filtro = new HashMap<>();
        usuarioTeste = new Usuario(ID_TESTE, NOME, EMAIL, SENHA);
        like = new Like(LONG, ID_POSTAGEM, ID_USUARIO);
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
        assertEquals(JAVA, response.getTema());
        assertEquals(BACKEND, response.getAreaAtuacao());
        assertEquals(INT, response.getLikes());
        assertEquals(usuario, response.getAutorPostagem());
        assertEquals(DATA_CADASTRO, response.getDataDeCadastro());

        verify(repository, times(1)).save(any(Postagem.class));

    }


    @Test
    public void testarExibirPostagensCaminhoPositivo() {
        when(repository.findAll()).thenReturn(List.of(postagem));

        List<Postagem> response = service.exibirPostagens(filtro);

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
        assertEquals(usuario, response.get(INT).getAutorPostagem());
        assertEquals(DATA_CADASTRO, response.get(INT).getDataDeCadastro());

        verify(repository, times(1)).findAll();

    }

    @Test
    public void testarExibirPostagensExceptionNaoExistemPostagensCadastradas() {
        when(repository.findAll()).thenReturn(List.of());

        RuntimeException exception = assertThrows(NaoExistemPostagensCadastradasException.class,
                () -> {
                    service.exibirPostagens(filtro);
                });


        assertEquals(NaoExistemPostagensCadastradasException.class, exception.getClass());
        assertEquals(NAO_EXISTEM_POSTAGENS_CADASTRADAS, exception.getMessage());

    }


    @Test
    public void testarAplicarFiltroDeBuscaPorArea() {
        filtro.put("area", "backend");
        when(repository.area(filtro.get("area"))).thenReturn(List.of(postagem));

        List<Postagem> response = service.aplicarFiltroDeBusca(List.of(postagem), filtro);

        assertNotNull(response);
        assertEquals(Postagem.class, response.get(INT).getClass());
        assertEquals(1, response.size());

        verify(repository, times(1)).area(filtro.get("area"));
        verify(repository, times(0)).tipo(filtro.get("tipo"));
        verify(repository, times(0)).tema(filtro.get("tema"));
        verify(repository, times(0)).autorPostagem(filtro.get("autorPostagem"));
        verify(repository, times(0)).dataDeCadastroRecente(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).dataDeCadastroAntiga(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).likesUsuario(usuario.getId());

    }

    @Test
    public void testarAplicarFiltroDeBuscaPorTipo() {
        filtro.put("tipo", "documentacao");
        when(repository.tipo(filtro.get("tipo"))).thenReturn(List.of(postagem));

        List<Postagem> response = service.aplicarFiltroDeBusca(List.of(postagem), filtro);

        assertNotNull(response);
        assertEquals(Postagem.class, response.get(INT).getClass());
        assertEquals(1, response.size());

        verify(repository, times(1)).tipo(filtro.get("tipo"));
        verify(repository, times(0)).area(filtro.get("area"));
        verify(repository, times(0)).tema(filtro.get("tema"));
        verify(repository, times(0)).autorPostagem(filtro.get("autorPostagem"));
        verify(repository, times(0)).dataDeCadastroRecente(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).dataDeCadastroAntiga(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).likesUsuario(usuario.getId());

    }

    @Test
    public void testarAplicarFiltroDeBuscaPorTema() {
        filtro.put("tema", "java");
        when(repository.tema(filtro.get("tema"))).thenReturn(List.of(postagem));

        List<Postagem> response = service.aplicarFiltroDeBusca(List.of(postagem), filtro);

        assertNotNull(response);
        assertEquals(Postagem.class, response.get(INT).getClass());
        assertEquals(1, response.size());

        verify(repository, times(1)).tema(filtro.get("tema"));
        verify(repository, times(0)).tipo(filtro.get("tipo"));
        verify(repository, times(0)).area(filtro.get("area"));
        verify(repository, times(0)).autorPostagem(filtro.get("autorPostagem"));
        verify(repository, times(0)).dataDeCadastroRecente(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).dataDeCadastroAntiga(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).likesUsuario(usuario.getId());

    }

    @Test
    public void testarAplicarFiltroDeBuscaPorAutorDaPostagem() {
        filtro.put("autorPostagem", ID_USUARIO);
        when(repository.autorPostagem(filtro.get("autorPostagem"))).thenReturn(List.of(postagem));

        List<Postagem> response = service.aplicarFiltroDeBusca(List.of(postagem), filtro);

        assertNotNull(response);
        assertEquals(Postagem.class, response.get(INT).getClass());
        assertEquals(1, response.size());

        verify(repository, times(1)).autorPostagem(filtro.get("autorPostagem"));
        verify(repository, times(0)).tema(filtro.get("tema"));
        verify(repository, times(0)).tipo(filtro.get("tipo"));
        verify(repository, times(0)).area(filtro.get("area"));
        verify(repository, times(0)).dataDeCadastroRecente(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).dataDeCadastroAntiga(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).likesUsuario(usuario.getId());

    }

    @Test
    public void testarAplicarFiltroPorBuscaPorDataDeCadastroRecente() {
        filtro.put("dataDeCadastro", "desc");
        when(repository.dataDeCadastroRecente(filtro.get("dataDeCadastro"))).thenReturn(List.of(postagem));

        List<Postagem> response = service.aplicarFiltroDeBusca(List.of(postagem), filtro);

        assertNotNull(response);
        assertEquals(Postagem.class, response.get(INT).getClass());
        assertEquals(1, response.size());

        verify(repository, times(1)).dataDeCadastroRecente(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).autorPostagem(filtro.get("autorPostagem"));
        verify(repository, times(0)).tema(filtro.get("tema"));
        verify(repository, times(0)).tipo(filtro.get("tipo"));
        verify(repository, times(0)).area(filtro.get("area"));
        verify(repository, times(0)).dataDeCadastroAntiga(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).likesUsuario(usuario.getId());

    }

    @Test
    public void testarAplicarFiltroDeBuscaPorDataDeCadastroAntiga() {
        filtro.put("dataDeCadastro", "asc");
        when(repository.dataDeCadastroAntiga(filtro.get("dataDeCadastro"))).thenReturn(List.of(postagem));

        List<Postagem> response = service.aplicarFiltroDeBusca(List.of(postagem), filtro);

        assertNotNull(response);
        assertEquals(Postagem.class, response.get(INT).getClass());
        assertEquals(1, response.size());

        verify(repository, times(1)).dataDeCadastroAntiga(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).dataDeCadastroRecente(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).autorPostagem(filtro.get("autorPostagem"));
        verify(repository, times(0)).tema(filtro.get("tema"));
        verify(repository, times(0)).tipo(filtro.get("tipo"));
        verify(repository, times(0)).area(filtro.get("area"));
        verify(repository, times(0)).likesUsuario(usuario.getId());

    }


    @Test
    public void testarAplicarFiltroDeBuscaPorLikesUsuario() {
        filtro.put("email", EMAIL);
        when(usuarioRepository.findByEmail(anyString())).thenReturn(ofNullable(usuario));
        when(repository.likesUsuario(usuario.getId())).thenReturn(List.of(postagem));

        List<Postagem> response = service.aplicarFiltroDeBusca(List.of(postagem), filtro);

        assertNotNull(response);
        assertEquals(Postagem.class, response.get(INT).getClass());
        assertEquals(1, response.size());

        verify(repository, times(1)).likesUsuario(usuario.getId());
        verify(repository, times(0)).autorPostagem(filtro.get("autorPostagem"));
        verify(repository, times(0)).tema(filtro.get("tema"));
        verify(repository, times(0)).tipo(filtro.get("tipo"));
        verify(repository, times(0)).area(filtro.get("area"));
        verify(repository, times(0)).dataDeCadastroRecente(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).dataDeCadastroAntiga(filtro.get("dataDeCadastro"));

    }

    @Test
    public void testarAplicarFiltroDeBuscaPorLikesUsuarioExceptionUsuarioNaoEncontrado() {
        filtro.put("email", "xablau");
        when(usuarioRepository.findByEmail(anyString())).thenReturn(empty());

        RuntimeException exception = assertThrows(UsuarioNaoCadastradoException.class,
                () -> {
                    service.aplicarFiltroDeBusca(List.of(postagem), filtro);
                });

        assertEquals(UsuarioNaoCadastradoException.class, exception.getClass());
        assertEquals(USUARIO_NAO_CADASTRADO, exception.getMessage());

        verify(repository, times(0)).likesUsuario(usuario.getId());
        verify(repository, times(0)).autorPostagem(filtro.get("autorPostagem"));
        verify(repository, times(0)).tema(filtro.get("tema"));
        verify(repository, times(0)).tipo(filtro.get("tipo"));
        verify(repository, times(0)).area(filtro.get("area"));
        verify(repository, times(0)).dataDeCadastroRecente(filtro.get("dataDeCadastro"));
        verify(repository, times(0)).dataDeCadastroAntiga(filtro.get("dataDeCadastro"));

    }

    @Test
    public void testarAtualizarPostagemCaminhoPositivo() {
        when(repository.findById(anyLong())).thenReturn(ofNullable(postagem));
        when(repository.save(any(Postagem.class))).thenReturn(postagem);

        Postagem response = service.atualizarPostagem(postagem.getId(), postagem, usuario);

        assertNotNull(response);
        assertEquals(Postagem.class, response.getClass());

        assertEquals(ID_POSTAGEM, response.getId());
        assertEquals(TITULO, response.getTitulo());
        assertEquals(DESCRICAO, response.getDescricao());
        assertEquals(DOCUMENTACAO, response.getTipo());
        assertEquals(JAVA, response.getTema());
        assertEquals(BACKEND, response.getAreaAtuacao());
        assertEquals(INT, response.getLikes());
        assertEquals(usuario, response.getAutorPostagem());
        assertEquals(DATA_CADASTRO, response.getDataDeCadastro());

        verify(repository, times(1)).save(any(Postagem.class));
        verify(repository, times(1)).findById(anyLong());

    }

    @Test
    public void testarAtualizarPostagemExceptionPostagemNaoEncontrada() {
        when(repository.findById(anyLong())).thenReturn(empty());

        RuntimeException exception = assertThrows(PostagemNaoEncontradaException.class,
                () -> {
                    service.atualizarPostagem(postagem.getId(), postagem, usuarioTeste);
                });

        assertEquals(PostagemNaoEncontradaException.class, exception.getClass());
        assertEquals(POSTAGEM_NAO_CADASTRADA, exception.getMessage());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).save(any(Postagem.class));
    }

    @Test
    public void testarAtualizarPostagemExceptionUsuarioNaoAutorizado() {
        when(repository.findById(anyLong())).thenReturn(ofNullable(postagem));

        RuntimeException exception = assertThrows(UsuarioNaoAutorizadoException.class,
                () -> {
                    service.atualizarPostagem(postagem.getId(), postagem, usuarioTeste);
                });

        assertEquals(UsuarioNaoAutorizadoException.class, exception.getClass());
        assertEquals(USUÁRIO_NAO_AUTORIZADO, exception.getMessage());


        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).save(any(Postagem.class));
    }


    @Test
    public void testarCurtirPostagemCaminhoPositivoSalvaLike() throws Exception {
        when(repository.findById(anyLong())).thenReturn(ofNullable(postagem));
        when(likeRepository.jaCurtiu(anyLong(), anyString())).thenReturn(empty());
        when(likeRepository.save(any(Like.class))).thenReturn(like);
        when(repository.save(any(Postagem.class))).thenReturn(postagem);

        Postagem response = service.curtirPostagem(LONG, usuario);

        assertNotNull(response);
        assertEquals(Postagem.class, response.getClass());

        verify(likeRepository, times(1)).jaCurtiu(anyLong(), anyString());
        verify(likeRepository, times(1)).save(any());
        verify(likeRepository, times(0)).deleteById(anyLong());
        verify(repository, times(1)).save(any());

    }

    @Test
    public void testarCurtirPostagemCaminhoPositivoDeletaLike() throws Exception {
        when(repository.findById(anyLong())).thenReturn(ofNullable(postagem));
        when(likeRepository.jaCurtiu(anyLong(), anyString())).thenReturn(ofNullable(like));
        doNothing().when(likeRepository).deleteById(anyLong());
        when(repository.save(any(Postagem.class))).thenReturn(postagem);

        Postagem response = service.curtirPostagem(LONG, usuario);

        assertNotNull(response);
        assertEquals(Postagem.class, response.getClass());

        verify(likeRepository, times(1)).jaCurtiu(anyLong(), anyString());
        verify(likeRepository, times(0)).save(any());
        verify(likeRepository, times(1)).deleteById(anyLong());
        verify(repository, times(1)).save(any());

    }

    @Test
    public void testarCurtirPostagemExceptionPostagemNaoEncontrada() throws Exception {
        when(repository.findById(anyLong())).thenReturn(empty());

        RuntimeException exception = assertThrows(PostagemNaoEncontradaException.class,
                () -> {
                    service.curtirPostagem(LONG, usuario);
                });

        assertEquals(PostagemNaoEncontradaException.class, exception.getClass());
        assertEquals(POSTAGEM_NAO_CADASTRADA, exception.getMessage());

        verify(likeRepository, times(0)).jaCurtiu(anyLong(), anyString());
        verify(likeRepository, times(0)).save(any());
        verify(likeRepository, times(0)).deleteById(anyLong());
        verify(repository, times(0)).save(any());

    }


    @Test
    public void testarDeletarPostagemCaminhoPositivo() {
        when(repository.findById(anyLong())).thenReturn(ofNullable(postagem));
        doNothing().when(repository).deleteById(anyLong());

        service.deletarPostagem(postagem.getId(), usuario);

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testarDeletarPostagemExceptionPostagemNaoEncontrada() {
        when(repository.findById(anyLong())).thenReturn(empty());

        RuntimeException exception = assertThrows(PostagemNaoEncontradaException.class,
                () -> {
                    service.deletarPostagem(postagem.getId(), usuarioTeste);
                });

        assertEquals(PostagemNaoEncontradaException.class, exception.getClass());
        assertEquals(POSTAGEM_NAO_CADASTRADA, exception.getMessage());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).deleteById(anyLong());
    }

    @Test
    public void testarDeletarPostagemExceptionUsuarioNaoAutorizado() {
        when(repository.findById(anyLong())).thenReturn(ofNullable(postagem));

        RuntimeException exception = assertThrows(UsuarioNaoAutorizadoException.class,
                () -> {
                    service.atualizarPostagem(postagem.getId(), postagem, usuarioTeste);
                });

        assertEquals(UsuarioNaoAutorizadoException.class, exception.getClass());
        assertEquals(USUÁRIO_NAO_AUTORIZADO, exception.getMessage());


        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).save(any(Postagem.class));
    }

    @Test
    public void exibirPostagemPorId() {
        when(repository.findById(anyLong())).thenReturn(ofNullable(postagem));

        Postagem postagemRetornada = service.exibirPostagemPorId(postagem.getId());

        verify(repository, times(1)).findById(anyLong());
        assertEquals(postagem, postagemRetornada);
    }

    @Test
    public void testarExibirPostagemPorIdExceptionPostagemNaoEncontrada() {
        when(repository.findById(anyLong())).thenReturn(empty());

        RuntimeException exception = assertThrows(PostagemNaoEncontradaException.class,
                () -> {
                    service.exibirPostagemPorId(postagem.getId());
                });

        assertEquals(PostagemNaoEncontradaException.class, exception.getClass());
        assertEquals(POSTAGEM_NAO_CADASTRADA, exception.getMessage());

        verify(repository, times(1)).findById(anyLong());

    }

}