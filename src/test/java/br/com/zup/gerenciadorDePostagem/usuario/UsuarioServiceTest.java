package br.com.zup.gerenciadorDePostagem.usuario;

import br.com.zup.gerenciadorDePostagem.exceptions.EmailJaCadastradoException;
import br.com.zup.gerenciadorDePostagem.exceptions.NaoExistemUsuariosCadastradosException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoAutorizadoException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoCadastradoException;
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
    public static final String SENHA_CRIPTOGRAFADA = "$2a$10$/76T1TrDeFsblWwigaMkqukGYpzNk0ZbslAIaQs3NqP0Ta.fkc6lS";
    public static final String EMAIL_JA_CADASTRADO = "Email já cadastrado";
    public static final String USUARIO_NAO_CADASTRADO = "Usuário não cadastrado";
    public static final int INDEX = 0;
    public static final String NAO_HA_USUARIOS_CADASTRADOS = "Não há usuários cadastrados";
    public static final String USUARIO_NAO_AUTORIZADO = "Usuário não autorizado";


    @MockBean
    private UsuarioRepository usuarioRepository;
    @MockBean
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UsuarioService usuarioService;


    private Usuario usuario;


    @BeforeEach
    void setUp() {
        usuario = new Usuario(ID_USUARIO, NOME, EMAIL, SENHA);
    }

    @Test
    public void testarCadastrarUsuarioCaminhoPositivo() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(encoder.encode(anyString())).thenReturn(SENHA_CRIPTOGRAFADA);

        Usuario response = usuarioService.cadastrarUsuario(usuario);

        assertNotNull(response);
        assertEquals(Usuario.class, response.getClass());

        assertEquals(ID_USUARIO, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(SENHA_CRIPTOGRAFADA, response.getSenha());

        verify(usuarioRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void testarCadastrarUsuarioExceptionEmailJaCadastrado() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(ofNullable(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        RuntimeException exception = assertThrows(EmailJaCadastradoException.class,
                () -> {usuarioService.cadastrarUsuario(usuario);});

        assertEquals(EmailJaCadastradoException.class, exception.getClass());
        assertEquals(EMAIL_JA_CADASTRADO, exception.getMessage());

        verify(usuarioRepository, times(1)).findByEmail(anyString());
        verify(usuarioRepository, times(0)).save(usuario);

    }

    @Test
    public void testarAtualizarUsuarioCaminhoPositivo() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(ofNullable(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(encoder.encode(anyString())).thenReturn(SENHA_CRIPTOGRAFADA);

        Usuario response = usuarioService.atualizarUsuario(EMAIL, usuario);

        assertNotNull(response);
        assertEquals(Usuario.class, response.getClass());

        assertEquals(ID_USUARIO, response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(SENHA_CRIPTOGRAFADA, response.getSenha());

        verify(usuarioRepository, times(1)).findByEmail(anyString());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));

    }

    @Test
    public void testarAtualizarUsuarioExceptionUsuarioNaoCadastrado() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        RuntimeException exception = assertThrows(UsuarioNaoCadastradoException.class,
                () -> {usuarioService.atualizarUsuario(EMAIL,usuario);});

        assertEquals(UsuarioNaoCadastradoException.class, exception.getClass());
        assertEquals(USUARIO_NAO_CADASTRADO, exception.getMessage());

        verify(usuarioRepository, times(1)).findByEmail(anyString());
        verify(usuarioRepository, times(0)).save(any(Usuario.class));

    }

    @Test
    public void testarExibirUsuariosCaminhoPositivo() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> response = usuarioService.exibirUsuarios();

        assertNotNull(response);
        assertEquals(Usuario.class, response.get(INDEX).getClass());
        assertEquals(1, response.size());

        assertEquals(ID_USUARIO, response.get(INDEX).getId());
        assertEquals(NOME, response.get(INDEX).getNome());
        assertEquals(EMAIL, response.get(INDEX).getEmail());
        assertEquals(SENHA, response.get(INDEX).getSenha());

        verify(usuarioRepository, times(1)).findAll();

    }

    @Test
    public void testarExibirUsuariosExceptionNaoExistemUsuariosCadastradas() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        RuntimeException exception = assertThrows(NaoExistemUsuariosCadastradosException.class,
                () -> {usuarioService.exibirUsuarios();});

        assertEquals(NaoExistemUsuariosCadastradosException.class, exception.getClass());
        assertEquals(NAO_HA_USUARIOS_CADASTRADOS, exception.getMessage());

        verify(usuarioRepository, times(1)).findAll();

    }

    @Test
    public void testarDeletarUsuarioCaminhoPositivo() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(usuario));
        doNothing().when(usuarioRepository).deleteById(anyString());

        usuarioService.deletarUsuario(EMAIL, ID_USUARIO);

        verify(usuarioRepository, times(1)).findByEmail(anyString());
        verify(usuarioRepository, times(1)).deleteById(anyString());

    }

    @Test
    public void testarDeletarUsuarioExceptionUsuarioNaoAutorizado() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(usuario));

        RuntimeException exception = assertThrows(UsuarioNaoAutorizadoException.class,
                () -> {usuarioService.deletarUsuario(EMAIL,ID_TESTE);});

        assertEquals(UsuarioNaoAutorizadoException.class, exception.getClass());
        assertEquals(USUARIO_NAO_AUTORIZADO, exception.getMessage());

        verify(usuarioRepository, times(1)).findByEmail(anyString());
        verify(usuarioRepository, times(0)).deleteById(anyString());

    }

    @Test
    public void testarDeletarUsuarioExceptionUsuarioNaoCadastrado() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(UsuarioNaoCadastradoException.class,
                () -> {usuarioService.deletarUsuario(EMAIL,ID_USUARIO);});

        assertEquals(UsuarioNaoCadastradoException.class, exception.getClass());
        assertEquals(USUARIO_NAO_CADASTRADO, exception.getMessage());

        verify(usuarioRepository, times(1)).findByEmail(anyString());
        verify(usuarioRepository, times(0)).deleteById(anyString());

    }

}