package br.com.zup.gerenciadorDePostagem.usuario;

import br.com.zup.gerenciadorDePostagem.exceptions.EmailJaCadastradoException;
import br.com.zup.gerenciadorDePostagem.exceptions.NaoExistemUsuariosCadastradosException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoCadastradoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UsuarioServiceTest {

    public static final String ID_USUARIO = "402880e67e97bc73017e97bdd9fa0000";
    public static final String ID_TESTE = "402880e67e97bc73017e97bdd9fa1111";
    public static final String NOME = "XABLAU";
    public static final String EMAIL = "xablau@zup.com.br";
    public static final String SENHA = "1234";
    public static final String EMAIL_JA_CADASTRADO = "Email já cadastrado";
    public static final String USUARIO_NAO_CADASTRADO = "O usuário não existe, favor Cadastrar";
    public static final int INDEX = 0;
    public static final String NAO_HA_USUARIOS_CADASTRADOS = "Não há usuários cadastrados";

    @MockBean
    private UsuarioRepository usuarioRepository;
    @MockBean
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UsuarioService usuarioService;


    private Usuario usuario;
    private Usuario usuarioTeste;


    @BeforeEach
    void setUp() {
        usuario = new Usuario(ID_USUARIO,NOME,EMAIL,SENHA);
        usuarioTeste = new Usuario(ID_TESTE,NOME,EMAIL,SENHA);
    }

    @Test
    public void testarCadastrarUsuarioCaminhoPositivo() {

    }

    @Test
     public void testarCadastrarUsuarioExceptionEmailJaCadastrado() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(ofNullable(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        RuntimeException exception = assertThrows(EmailJaCadastradoException.class,
                () -> {usuarioService.cadastrarUsuario(usuario);});

        assertEquals(EmailJaCadastradoException.class, exception.getClass());
        assertEquals(EMAIL_JA_CADASTRADO, exception.getMessage());

        verify(usuarioRepository,times(1)).findByEmail(anyString());
        verify(usuarioRepository,times(0)).save(usuario);

    }

    @Test
    public void testarAtualizarUsuarioCaminhoPositivo() {
        when(usuarioRepository.findById(anyString())).thenReturn(ofNullable(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario response = usuarioService.atualizarUsuario(ID_USUARIO, usuario);

        assertNotNull(response);
        assertEquals(Usuario.class,response.getClass());

        assertEquals(ID_USUARIO,response.getId());
        assertEquals(NOME,response.getNome());
        assertEquals(EMAIL,response.getEmail());
        assertEquals(SENHA,response.getSenha());

        verify(usuarioRepository,times(1)).findById(anyString());
        verify(usuarioRepository,times(1)).save(any(Usuario.class));

    }

    @Test
    public void testarAtualizarUsuarioExceptionUsuarioNaoCadastrado() {
        when(usuarioRepository.findById(anyString())).thenReturn(empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        RuntimeException exception = assertThrows(UsuarioNaoCadastradoException.class,
                () -> {usuarioService.atualizarUsuario(ID_USUARIO,usuario);});

        assertEquals(UsuarioNaoCadastradoException.class, exception.getClass());
        assertEquals(USUARIO_NAO_CADASTRADO, exception.getMessage());

        verify(usuarioRepository,times(1)).findById(anyString());
        verify(usuarioRepository,times(0)).save(any(Usuario.class));

    }

    @Test
    public void testarExibirUsuariosCaminhoPositivo() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> response = usuarioService.exibirUsuarios();

        assertNotNull(response);
        assertEquals(Usuario.class,response.get(INDEX).getClass());
        assertEquals(1, response.size());

        assertEquals(ID_USUARIO,response.get(INDEX).getId());
        assertEquals(NOME,response.get(INDEX).getNome());
        assertEquals(EMAIL,response.get(INDEX).getEmail());
        assertEquals(SENHA,response.get(INDEX).getSenha());

        verify(usuarioRepository,times(1)).findAll();

    }

    @Test
    public void testarExibirUsuariosExceptionNaoExistemUsuariosCadastradas() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        RuntimeException exception = assertThrows(NaoExistemUsuariosCadastradosException.class,
                () -> {usuarioService.exibirUsuarios();});

        assertEquals(NaoExistemUsuariosCadastradosException.class,exception.getClass());
        assertEquals(NAO_HA_USUARIOS_CADASTRADOS, exception.getMessage());

        verify(usuarioRepository,times(1)).findAll();

    }

    @Test
    public void testarDeletarUsuarioCaminhoPositivo() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(usuario));
        doNothing().when(usuarioRepository).deleteById(anyString());

        usuarioService.deletarUsuario(EMAIL,ID_USUARIO);

        verify(usuarioRepository,times(1)).findByEmail(anyString());
        verify(usuarioRepository,times(1)).deleteById(anyString());

    }

}