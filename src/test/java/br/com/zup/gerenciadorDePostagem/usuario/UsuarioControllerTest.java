package br.com.zup.gerenciadorDePostagem.usuario;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {
    @MockBean
    private UsuarioService usuarioService;

   @Autowired
    private MockMvc mockMvc;

   private ObjectMapper objectMapper;
   private Usuario usuario;
   private List<Usuario>usuarios;

   @BeforeEach
    private void setup(){
       objectMapper = new ObjectMapper();

       usuario = new Usuario();
       usuario.setNome("Ana");
       usuario.setEmail("ana@zup.com.br");
       usuario.setSenha("123456");
       usuario.setId("1");

       usuarios = Arrays.asList(usuario);
   }
}
