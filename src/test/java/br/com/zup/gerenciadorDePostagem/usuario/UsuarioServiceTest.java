package br.com.zup.gerenciadorDePostagem.usuario;

import br.com.zup.gerenciadorDePostagem.exceptions.EmailJaCadastradoException;
import br.com.zup.gerenciadorDePostagem.postagem.Postagem;
import lombok.NoArgsConstructor;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static br.com.zup.gerenciadorDePostagem.enums.Area.BACKEND;
import static br.com.zup.gerenciadorDePostagem.enums.Tema.JAVA;
import static br.com.zup.gerenciadorDePostagem.enums.Tipo.DOCUMENTACAO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UsuarioServiceTest {

    public static final String ID_USUARIO = "402880e67e97bc73017e97bdd9fa0000";
    public static final String ID_TESTE = "402880e67e97bc73017e97bdd9fa1111";
    public static final String NOME = "XABLAU";
    public static final String EMAIL = "xablau@zup.com.br";
    public static final String SENHA = "1234";
    public static final String EMAIL_JA_CADASTRADO = "Email já cadastrado";

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
    void testarCadastrarUsuarioCaminhoPositivo() {

    }

    @Test
    void testarCadastrarUsuarioExceptionEmailJaCadastrado() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        RuntimeException exception = assertThrows(EmailJaCadastradoException.class,
                () -> {usuarioService.cadastrarUsuario(usuario);});

        assertEquals(EmailJaCadastradoException.class, exception.getClass());
        assertEquals(EMAIL_JA_CADASTRADO, exception.getMessage());

        verify(usuarioRepository,times(1)).findByEmail(anyString());
        verify(usuarioRepository,times(0)).save(usuario);

    }

    @Test
    void testarAtualizarUsuarioCaminhoPositivo() {
    }

    @Test
    void exibirUsuarios() {
    }

    @Test
    void deletarUsuario() {
    }

}