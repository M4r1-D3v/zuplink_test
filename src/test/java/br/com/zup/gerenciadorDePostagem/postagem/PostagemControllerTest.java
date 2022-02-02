package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.components.ConversorAutenticacao;
import br.com.zup.gerenciadorDePostagem.config.security.UsuarioLoginService;
import br.com.zup.gerenciadorDePostagem.config.security.jwt.JWTComponent;
import br.com.zup.gerenciadorDePostagem.exceptions.NaoExistemPostagensCadastradasException;
import br.com.zup.gerenciadorDePostagem.exceptions.PostagemNaoEncontradaException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoAutorizadoException;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.AtualizarPostagemDTO;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.PostagemDTO;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.PostagensCadastradasDTO;
import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
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

import java.time.LocalDate;
import java.util.List;

import static br.com.zup.gerenciadorDePostagem.enums.Area.BACKEND;
import static br.com.zup.gerenciadorDePostagem.enums.Tema.JAVA;
import static br.com.zup.gerenciadorDePostagem.enums.Tipo.DOCUMENTACAO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({PostagemController.class, ModelMapper.class, JWTComponent.class, UsuarioLoginService.class})
class PostagemControllerTest {

    public static final String TITULO = "Titulo";
    public static final String DESCRICAO = "Descricao";
    public static final String LINK = "https://www.zup.com.br/";
    public static final String NOME = "Xablau";
    public static final String ID_USUARIO = "402880e67e97bc73017e97bdd9fa0001";
    public static final String EMAIL_USUARIO = "email@zup.com.br";
    public static final String SENHA = "1234";
    public static final long ID_POSTAGEM = 1L;
    public static final int INT = 0;
    public static final String DESCRICAO_SIZE = "Lorem Ipsum is simply dummy text of the printing and typesetting" +
            " industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an" +
            " unknown" + "printer took a galley of type and scrambled it to make a type specimen book. It has" +
            " survived not only five centuries, but also the leap into electronic typesetting, remaining" +
            " essentially unchanged.";


    @MockBean
    private PostagemService service;
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
    private Postagem postagem;
    private PostagemDTO postagemDTO;
    private AtualizarPostagemDTO atualizarPostagemDTO;
    private Usuario usuario;


    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        usuario = new Usuario(ID_USUARIO, NOME, EMAIL_USUARIO, SENHA);
        postagem = new Postagem(ID_POSTAGEM, TITULO, DESCRICAO, LINK,
                DOCUMENTACAO, JAVA, BACKEND, INT, usuario, LocalDate.now());
        postagemDTO = new PostagemDTO(TITULO, DESCRICAO, LINK,
                DOCUMENTACAO, JAVA, BACKEND);
        atualizarPostagemDTO = new AtualizarPostagemDTO(TITULO, DESCRICAO, DOCUMENTACAO, JAVA, BACKEND);
    }


    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaCadastrarPostagemCaminhoPositivo() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(PostagemDTO.class), any())).thenReturn(postagem);
        when(service.salvarPostagem(any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        String json = objectMapper.writeValueAsString(postagemDTO);

        ResultActions response = mockMvc.perform(post("/postagem").content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isCreated());

        assertEquals(201, response.andReturn().getResponse().getStatus());
        verify(service, times(1)).salvarPostagem(any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaCadastrarPostagemValidacaoTituloNotBlank() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(PostagemDTO.class), any())).thenReturn(postagem);
        when(service.salvarPostagem(any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        postagemDTO.setTitulo("");
        String json = objectMapper.writeValueAsString(postagemDTO);

        ResultActions response = mockMvc.perform(post("/postagem").content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0)).salvarPostagem(any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaCadastrarPostagemValidacaoTituloNotNull() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(PostagemDTO.class), any())).thenReturn(postagem);
        when(service.salvarPostagem(any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        postagemDTO.setTitulo(null);
        String json = objectMapper.writeValueAsString(postagemDTO);

        ResultActions response = mockMvc.perform(post("/postagem").content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0)).salvarPostagem(any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaCadastrarPostagemValidacaoTituloSizeMin() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(PostagemDTO.class), any())).thenReturn(postagem);
        when(service.salvarPostagem(any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        postagemDTO.setTitulo("Ti");
        String json = objectMapper.writeValueAsString(postagemDTO);

        ResultActions response = mockMvc.perform(post("/postagem").content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0)).salvarPostagem(any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaCadastrarPostagemValidacaoDescricaoSizeMax() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(PostagemDTO.class), any())).thenReturn(postagem);
        when(service.salvarPostagem(any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        postagemDTO.setDescricao(DESCRICAO_SIZE);
        String json = objectMapper.writeValueAsString(postagemDTO);

        ResultActions response = mockMvc.perform(post("/postagem").content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0)).salvarPostagem(any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaCadastrarPostagemValidacaoLinkNotBlank() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(PostagemDTO.class), any())).thenReturn(postagem);
        when(service.salvarPostagem(any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        postagemDTO.setLink("");
        String json = objectMapper.writeValueAsString(postagemDTO);

        ResultActions response = mockMvc.perform(post("/postagem").content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0)).salvarPostagem(any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaCadastrarPostagemValidacaoLinkNotNull() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(PostagemDTO.class), any())).thenReturn(postagem);
        when(service.salvarPostagem(any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        postagemDTO.setLink(null);
        String json = objectMapper.writeValueAsString(postagemDTO);

        ResultActions response = mockMvc.perform(post("/postagem").content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0)).salvarPostagem(any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaCadastrarPostagemValidacaoLinkPadrao() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(PostagemDTO.class), any())).thenReturn(postagem);
        when(service.salvarPostagem(any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        postagemDTO.setLink("xablau");
        String json = objectMapper.writeValueAsString(postagemDTO);

        ResultActions response = mockMvc.perform(post("/postagem").content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0)).salvarPostagem(any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaCadastrarPostagemValidacaoTemaNotNull() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(PostagemDTO.class), any())).thenReturn(postagem);
        when(service.salvarPostagem(any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        postagemDTO.setTema(null);
        String json = objectMapper.writeValueAsString(postagemDTO);

        ResultActions response = mockMvc.perform(post("/postagem").content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0)).salvarPostagem(any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaCadastrarPostagemValidacaoTipoNotNull() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(PostagemDTO.class), any())).thenReturn(postagem);
        when(service.salvarPostagem(any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        postagemDTO.setTipo(null);
        String json = objectMapper.writeValueAsString(postagemDTO);

        ResultActions response = mockMvc.perform(post("/postagem").content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0)).salvarPostagem(any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaCadastrarPostagemValidacaoAreaAtuacaoNotNull() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(PostagemDTO.class), any())).thenReturn(postagem);
        when(service.salvarPostagem(any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        postagemDTO.setAreaAtuacao(null);
        String json = objectMapper.writeValueAsString(postagemDTO);

        ResultActions response = mockMvc.perform(post("/postagem").content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0)).salvarPostagem(any(Postagem.class), any(Usuario.class));

    }

    @Test
    public void testarRotaParaExibirPostagensCadastradasCaminhoPositivo() throws Exception {
        when(service.exibirPostagens(any())).thenReturn(List.of(postagem));

        ResultActions response = mockMvc.perform(get("/postagem")
                        .contentType(APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        String jsonResposta = response.andReturn().getResponse().getContentAsString();
        List<PostagensCadastradasDTO> postagens = objectMapper.readValue(jsonResposta,
                new TypeReference<List<PostagensCadastradasDTO>>() {
                });

        assertEquals(200, response.andReturn().getResponse().getStatus());
        verify(service, times(1)).exibirPostagens(any());

    }

    @Test
    public void testarRotaParaExibirPostagensNaoExistemPostagemCadastradas() throws Exception {
        doThrow(NaoExistemPostagensCadastradasException.class).when(service).exibirPostagens(any());

        ResultActions response = mockMvc.perform(get("/postagem")
                .contentType(APPLICATION_JSON)).andExpect(status().isNotFound());


        assertEquals(404, response.andReturn().getResponse().getStatus());
        verify(service, times(1)).exibirPostagens(any());

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaEditarPostagemCaminhoPositivo() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(AtualizarPostagemDTO.class), any())).thenReturn(postagem);
        when(service.atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        String json = objectMapper.writeValueAsString(atualizarPostagemDTO);

        ResultActions response = mockMvc.perform(put("/postagem/" + postagem.getId()).content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isOk());

        assertEquals(200, response.andReturn().getResponse().getStatus());
        verify(service, times(1))
                .atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaEditarPostagemValidacaoTituloNotBlank() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(AtualizarPostagemDTO.class), any())).thenReturn(postagem);
        when(service.atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        atualizarPostagemDTO.setTitulo("");
        String json = objectMapper.writeValueAsString(atualizarPostagemDTO);

        ResultActions response = mockMvc.perform(put("/postagem/" + postagem.getId()).content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0))
                .atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaEditarPostagemValidacaoTituloNotNull() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(AtualizarPostagemDTO.class), any())).thenReturn(postagem);
        when(service.atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        atualizarPostagemDTO.setTitulo(null);
        String json = objectMapper.writeValueAsString(atualizarPostagemDTO);

        ResultActions response = mockMvc.perform(put("/postagem/" + postagem.getId()).content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0))
                .atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaEditarPostagemValidacaoTituloSizeMin() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(AtualizarPostagemDTO.class), any())).thenReturn(postagem);
        when(service.atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        atualizarPostagemDTO.setTitulo("Ti");
        String json = objectMapper.writeValueAsString(atualizarPostagemDTO);

        ResultActions response = mockMvc.perform(put("/postagem/" + postagem.getId()).content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0))
                .atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaEditarPostagemValidacaoDescricaoSizeMax() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(AtualizarPostagemDTO.class), any())).thenReturn(postagem);
        when(service.atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        atualizarPostagemDTO.setDescricao(DESCRICAO_SIZE);
        String json = objectMapper.writeValueAsString(atualizarPostagemDTO);

        ResultActions response = mockMvc.perform(put("/postagem/" + postagem.getId()).content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0))
                .atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaEditarPostagemValidacaoTipoNotNull() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(AtualizarPostagemDTO.class), any())).thenReturn(postagem);
        when(service.atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        atualizarPostagemDTO.setTipo(null);
        String json = objectMapper.writeValueAsString(atualizarPostagemDTO);

        ResultActions response = mockMvc.perform(put("/postagem/" + postagem.getId()).content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0))
                .atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaEditarPostagemValidacaoTemaNotNull() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(AtualizarPostagemDTO.class), any())).thenReturn(postagem);
        when(service.atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        atualizarPostagemDTO.setTema(null);
        String json = objectMapper.writeValueAsString(atualizarPostagemDTO);

        ResultActions response = mockMvc.perform(put("/postagem/" + postagem.getId()).content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0))
                .atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaEditarPostagemValidacaoAreaAtuacaoNotNull() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(AtualizarPostagemDTO.class), any())).thenReturn(postagem);
        when(service.atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class))).thenReturn(postagem);

        atualizarPostagemDTO.setAreaAtuacao(null);
        String json = objectMapper.writeValueAsString(atualizarPostagemDTO);

        ResultActions response = mockMvc.perform(put("/postagem/" + postagem.getId()).content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());

        assertEquals(422, response.andReturn().getResponse().getStatus());
        verify(service, times(0))
                .atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class));

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaEditarPostagemPostagemNaoEncontrada() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(AtualizarPostagemDTO.class), any())).thenReturn(postagem);
        doThrow(PostagemNaoEncontradaException.class).when(service)
                .atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class));

        String json = objectMapper.writeValueAsString(atualizarPostagemDTO);

        ResultActions response = mockMvc.perform(put("/postagem/" + postagem.getId()).content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isNotFound());


        assertEquals(404, response.andReturn().getResponse().getStatus());
        verify(service, times(1)).atualizarPostagem(anyLong(), any(), any());

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaEditarPostagemUsuarioNaoAutorizado() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any(Authentication.class))).thenReturn(usuario);
        when(modelMapper.map(any(AtualizarPostagemDTO.class), any())).thenReturn(postagem);
        doThrow(UsuarioNaoAutorizadoException.class).when(service)
                .atualizarPostagem(anyLong(), any(Postagem.class), any(Usuario.class));

        String json = objectMapper.writeValueAsString(atualizarPostagemDTO);

        ResultActions response = mockMvc.perform(put("/postagem/" + postagem.getId()).content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isForbidden());


        assertEquals(403, response.andReturn().getResponse().getStatus());
        verify(service, times(1)).atualizarPostagem(anyLong(), any(), any());

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaExcluirPostagemCaminhoPositivo() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any())).thenReturn(usuario);
        doNothing().when(service).deletarPostagem(anyLong(), any(Usuario.class));

        ResultActions response = mockMvc.perform(delete("/postagem/" + postagem.getId())
                .contentType(APPLICATION_JSON)).andExpect(status().isNoContent());

        assertEquals(204, response.andReturn().getResponse().getStatus());
        verify(service, times(1)).deletarPostagem(anyLong(), any());

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaExcluirPostagemNaoCadastrada() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any())).thenReturn(usuario);
        doThrow(PostagemNaoEncontradaException.class).when(service).deletarPostagem(anyLong(), any());

        ResultActions response = mockMvc.perform(delete("/postagem/" + postagem.getId())
                .contentType(APPLICATION_JSON)).andExpect(status().isNotFound());


        assertEquals(404, response.andReturn().getResponse().getStatus());
        verify(service, times(1)).deletarPostagem(anyLong(), any());

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO, password = SENHA)
    public void testarRotaParaExcluirPostagemUsuarioNaoAutorizado() throws Exception {
        when(conversorAutenticacao.converterAutenticacao(any())).thenReturn(usuario);
        doThrow(UsuarioNaoAutorizadoException.class).when(service).deletarPostagem(anyLong(), any(Usuario.class));

        ResultActions response = mockMvc.perform(delete("/postagem/" + postagem.getId())
                .contentType(APPLICATION_JSON)).andExpect(status().isForbidden());


        assertEquals(403, response.andReturn().getResponse().getStatus());
        verify(service, times(1)).deletarPostagem(anyLong(), any());

    }

}