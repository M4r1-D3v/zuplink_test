package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.components.ConversorAutenticacao;
import br.com.zup.gerenciadorDePostagem.config.security.UsuarioLoginService;
import br.com.zup.gerenciadorDePostagem.config.security.jwt.JWTComponent;
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

    @MockBean
    private PostagemService service;
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private JWTComponent jwtComponent;
    @MockBean
    private UsuarioLoginService usuarioLoginService;
    @MockBean
    private ConversorAutenticacao conversorAutenticacaoService;


    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private Postagem postagem;
    private PostagemDTO postagemDTO;
    private Usuario usuario;


    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        usuario = new Usuario(ID_USUARIO, NOME, EMAIL_USUARIO, SENHA);
        postagem = new Postagem(ID_POSTAGEM, TITULO, DESCRICAO, LINK,
                DOCUMENTACAO, JAVA, BACKEND, INT, INT, usuario, LocalDate.now());
        postagemDTO = new PostagemDTO(TITULO, DESCRICAO, LINK,
                DOCUMENTACAO, JAVA, BACKEND);
    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO,password = SENHA)
    public void testarRotaParaCadastrarPostagemCaminhoPositivo() throws Exception{
        when(service.salvarPostagem(any(Postagem.class), any(Usuario.class))).thenReturn(postagem);
        String json = objectMapper.writeValueAsString(postagemDTO);


        ResultActions response = mockMvc.perform(post("/postagem").content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isCreated());


        assertEquals(201, response.andReturn().getResponse().getStatus());

    }

    @Test
    public void testarRotaParaExibirPostagensCadastradasCaminhoPositivo() throws Exception {
        when(service.exibirPostagens()).thenReturn(List.of(postagem));

        ResultActions response = mockMvc.perform(get("/postagem")
                .contentType(APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        String jsonResposta = response.andReturn().getResponse().getContentAsString();
        List<PostagensCadastradasDTO> postagens = objectMapper.readValue(jsonResposta,
                new TypeReference<List<PostagensCadastradasDTO>>() {});

        verify(service, times(1)).exibirPostagens();

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO,password = SENHA)
    public void testarRotaParaEditarPostagemCaminhoPositivo() throws Exception{
        when(service.atualizarPostagem(anyLong(),any(Postagem.class),any(Usuario.class))).thenReturn(postagem);
        String json = objectMapper.writeValueAsString(postagemDTO);

        ResultActions response = mockMvc.perform(put("/postagem/" + postagem.getId()).content(json)
                .contentType(APPLICATION_JSON)).andExpect(status().isOk());

        assertEquals(200, response.andReturn().getResponse().getStatus());

    }

    @Test
    @WithMockUser(username = EMAIL_USUARIO,password = SENHA)
    public void testarRotaParaExcluirPostagemCaminhoPositivo() throws Exception {
        doNothing().when(service).deletarPostagem(anyLong(),any(Usuario.class));

        ResultActions response = mockMvc.perform(delete("/postagem/" + postagem.getId())
                .contentType(APPLICATION_JSON)).andExpect(status().isNoContent());

        assertEquals(204,response.andReturn().getResponse().getStatus());
        verify(service,times(1)).deletarPostagem(anyLong(),any());

    }
}