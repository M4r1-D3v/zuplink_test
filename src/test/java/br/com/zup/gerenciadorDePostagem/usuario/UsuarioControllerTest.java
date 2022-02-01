package br.com.zup.gerenciadorDePostagem.usuario;


import br.com.zup.gerenciadorDePostagem.components.ConversorAutenticacao;
import br.com.zup.gerenciadorDePostagem.config.security.UsuarioLoginService;
import br.com.zup.gerenciadorDePostagem.config.security.jwt.JWTComponent;
import br.com.zup.gerenciadorDePostagem.exceptions.EmailJaCadastradoException;
import br.com.zup.gerenciadorDePostagem.exceptions.NaoExistemUsuariosCadastradosException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoCadastradoException;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.PostagensCadastradasDTO;
import br.com.zup.gerenciadorDePostagem.usuario.dtos.UsuarioDto;
import br.com.zup.gerenciadorDePostagem.usuario.dtos.UsuarioSaidaDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UsuarioController.class, ModelMapper.class, JWTComponent.class, UsuarioLoginService.class})
public class UsuarioControllerTest {

    public static final String NOME = "Ana";
    public static final String EMA = "ana@zup.com.br";
    public static final String SENHA = "1234";
    public static final String ID_USUARIO = "1";
    private static final String EMAIL_USUARIO = "email@zup.com.br";


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
        when(modelMapper.map(any(UsuarioDto.class), any())).thenReturn(usuario);
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
        when(modelMapper.map(any(UsuarioDto.class), any())).thenReturn(usuario);
        when(usuarioService.cadastrarUsuario(any(Usuario.class))).thenReturn(usuario);

        usuarioDto.setEmail("email@email");
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoEmailNotNull() throws Exception {
        when(modelMapper.map(any(UsuarioDto.class), any())).thenReturn(usuario);
        when(usuarioService.cadastrarUsuario(any(Usuario.class))).thenReturn(usuario);

        usuarioDto.setEmail(null);
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoSenhaNotNull() throws Exception {
        when(modelMapper.map(any(UsuarioDto.class), any())).thenReturn(usuario);
        when(usuarioService.cadastrarUsuario(any(Usuario.class))).thenReturn(usuario);

        usuarioDto.setSenha(null);
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoSenhaNotBlank() throws Exception {
        when(modelMapper.map(any(UsuarioDto.class), any())).thenReturn(usuario);
        when(usuarioService.cadastrarUsuario(any(Usuario.class))).thenReturn(usuario);

        usuarioDto.setSenha("");
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoSenhaSizeMin() throws Exception {
        when(modelMapper.map(any(UsuarioDto.class), any())).thenReturn(usuario);
        when(usuarioService.cadastrarUsuario(any(Usuario.class))).thenReturn(usuario);

        usuarioDto.setSenha("123");
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoNomeNotNull() throws Exception {
        when(modelMapper.map(any(UsuarioDto.class), any())).thenReturn(usuario);
        when(usuarioService.cadastrarUsuario(any(Usuario.class))).thenReturn(usuario);

        usuarioDto.setNome(null);
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoNomeNotBlank() throws Exception {
        when(modelMapper.map(any(UsuarioDto.class), any())).thenReturn(usuario);
        when(usuarioService.cadastrarUsuario(any(Usuario.class))).thenReturn(usuario);

        usuarioDto.setNome("");
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastroDeUsuarioValidacaoNomeSizeMin() throws Exception {
        when(modelMapper.map(any(UsuarioDto.class), any())).thenReturn(usuario);
        when(usuarioService.cadastrarUsuario(any(Usuario.class))).thenReturn(usuario);

        usuarioDto.setNome("J");
        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());


        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(0)).cadastrarUsuario(any());

    }

    @Test
    public void testarRotaParaCadastrarUsuarioExceptionEmailJaCadastrado() throws Exception {
        when(modelMapper.map(any(), any())).thenReturn(usuario);
        doThrow(EmailJaCadastradoException.class).when(usuarioService).cadastrarUsuario(usuario);

        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(1)).cadastrarUsuario(any());

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaAtualizarUsuarioCaminhoPositivo() throws Exception{
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(UsuarioDto.class), any())).thenReturn(usuario);
        when(usuarioService.atualizarUsuario(anyString(),any(Usuario.class))).thenReturn(usuario);

        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(put("/usuario")
                .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        assertEquals(200, response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(1)).atualizarUsuario(anyString(),any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaAtualizarUsuarioExceptionUsuarioNaoCadastrado() throws Exception{
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(UsuarioDto.class), any())).thenReturn(usuario);
        doThrow(UsuarioNaoCadastradoException.class).when(usuarioService)
                .atualizarUsuario(anyString(),any(Usuario.class));

        String json = objectMapper.writeValueAsString(usuarioDto);

        ResultActions response = mockMvc.perform(put("/usuario")
                        .contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());

        assertEquals(404, response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(1)).atualizarUsuario(anyString(),any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaExibirUsuariosCadastradasCaminhoPositivo() throws Exception {
        when(usuarioService.exibirUsuarios()).thenReturn(List.of(usuario));

        ResultActions response = mockMvc.perform(get("/usuario")
                        .contentType(APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        String jsonResposta = response.andReturn().getResponse().getContentAsString();
        List<UsuarioSaidaDTO> postagens = objectMapper.readValue(jsonResposta,
                new TypeReference<List<UsuarioSaidaDTO>>() {});

        assertEquals(200, response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(1)).exibirUsuarios();

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaExibirUsuariosCadastradasExceptionNaoExistemUsuariosCadastrados() throws Exception {
        doThrow(NaoExistemUsuariosCadastradosException.class).when(usuarioService).exibirUsuarios();

        ResultActions response = mockMvc.perform(get("/usuario")
                        .contentType(APPLICATION_JSON)).andExpect(status().isNotFound());

        assertEquals(404, response.andReturn().getResponse().getStatus());
        verify(usuarioService, times(1)).exibirUsuarios();

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaDeletarUsuarioCaminhoPositivo() throws Exception{
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        doNothing().when(usuarioService).deletarUsuario(anyString(),anyString());

        ResultActions response = mockMvc.perform(delete("/usuario?email=" + EMAIL_USUARIO)
                .contentType(APPLICATION_JSON)).andExpect(status().isNoContent());

        assertEquals(204,response.andReturn().getResponse().getStatus());
        verify(usuarioService,times(1)).deletarUsuario(anyString(),anyString());

    }

}
