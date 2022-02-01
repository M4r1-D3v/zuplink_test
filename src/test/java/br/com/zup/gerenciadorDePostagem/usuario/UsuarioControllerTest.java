package br.com.zup.gerenciadorDePostagem.usuario;


import br.com.zup.gerenciadorDePostagem.components.ConversorAutenticacao;
import br.com.zup.gerenciadorDePostagem.config.security.UsuarioLoginService;
import br.com.zup.gerenciadorDePostagem.config.security.jwt.JWTComponent;
import br.com.zup.gerenciadorDePostagem.exceptions.EmailJaCadastradoException;
import br.com.zup.gerenciadorDePostagem.usuario.dtos.UsuarioDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({UsuarioController.class, ModelMapper.class, JWTComponent.class, UsuarioLoginService.class})
public class UsuarioControllerTest {

    public static final String NOME = "Ana";
    public static final String EMA = "ana@zup.com.br";
    public static final String SENHA = "1234";
    public static final String ID_USUARIO = "1";


    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private JWTComponent jwtComponent;
    @MockBean
    private UsuarioLoginService usuarioLoginService;
    @MockBean
    private ConversorAutenticacao conversorAutenticacao;

    @Autowired
    private MockMvc mockMvc;


    private ObjectMapper objectMapper;
    private Usuario usuario;
    private UsuarioDto usuarioDto;

    @BeforeEach
    private void setup() {
        objectMapper = new ObjectMapper();

        usuario = new Usuario();
        usuario.setNome(NOME);
        usuario.setEmail(EMA);
        usuario.setSenha(SENHA);
        usuario.setId(ID_USUARIO);

        usuarioDto = new UsuarioDto();
        usuarioDto.setNome(NOME);
        usuarioDto.setEmail(EMA);
        usuarioDto.setSenha(SENHA);

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioCaminhoPositivo() throws Exception {
        when(usuarioService.cadastrarUsuario(any(Usuario.class))).thenReturn(usuario);
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions resultActions = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().is(201)
                );

        verify(usuarioService, times(1)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoEmailForaPadrao() throws Exception {
        usuarioDto.setEmail("email@email");
        when(usuarioService.cadastrarUsuario(any())).thenReturn(usuario);
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(422,response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoEmailNotNull() throws Exception {
        usuarioDto.setEmail(null);
        when(usuarioService.cadastrarUsuario(any())).thenReturn(usuario);
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422,response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoSenhaNotNull() throws Exception {
        usuarioDto.setSenha(null);
        when(usuarioService.cadastrarUsuario(any())).thenReturn(usuario);
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422,response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoSenhaNotBlank() throws Exception {
        usuarioDto.setSenha("");
        when(usuarioService.cadastrarUsuario(any())).thenReturn(usuario);
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422,response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoSenhaSizeMin() throws Exception {
        usuarioDto.setSenha("123");
        when(usuarioService.cadastrarUsuario(any())).thenReturn(usuario);
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422,response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoNomeNotNull() throws Exception {
        usuarioDto.setNome(null);
        when(usuarioService.cadastrarUsuario(any())).thenReturn(usuario);
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422,response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoNomeNotBlank() throws Exception {
        usuarioDto.setNome("");
        when(usuarioService.cadastrarUsuario(any())).thenReturn(usuario);
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422,response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoNomeSizeMin() throws Exception {
        usuarioDto.setNome("J");
        when(usuarioService.cadastrarUsuario(any())).thenReturn(usuario);
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422,response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

   @Test
    public void testarRotaParaCadastrarUsuarioExceptionEmailJaCadastrado() throws Exception{
        when(modelMapper.map(any(),any())).thenReturn(usuario);
        doThrow(EmailJaCadastradoException.class).when(usuarioService).cadastrarUsuario(usuario);
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());

       assertEquals(422,response.andReturn().getResponse().getStatus());
       verify(usuarioService, times(1)).cadastrarUsuario(any());

   }

}
