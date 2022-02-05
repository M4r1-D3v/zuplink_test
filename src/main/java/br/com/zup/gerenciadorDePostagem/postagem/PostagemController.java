package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.components.ConversorAutenticacao;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.AtualizarPostagemDTO;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.PostagemDTO;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.RetornoPostagemDTO;
import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(value = "Gerenciamento de links de postagem.")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/postagem")
public class PostagemController {

    @Autowired
    private PostagemService postagemService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ConversorAutenticacao conversorAutenticacao;


    @PostMapping
    @ApiOperation(value="Cadastrar postagem")
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrarPostagem(@RequestBody @Valid PostagemDTO cadastroPostagemDTO,
                                  Authentication authentication) {

        Usuario usuario = conversorAutenticacao.converterAutenticacao(authentication);
        postagemService.salvarPostagem(modelMapper.map(cadastroPostagemDTO, Postagem.class), usuario);

    }


    @GetMapping
    @ApiOperation(value="Buscar postagem")
    public List<RetornoPostagemDTO> exibirPostagensCadastradas(@RequestParam(required = false)
                                                                            Map<String, String> filtros) {

        List<RetornoPostagemDTO> postagensCadastradasDTO = new ArrayList<>();

        for (Postagem postagem : postagemService.exibirPostagens(filtros)) {
            postagensCadastradasDTO.add(modelMapper.map(postagem, RetornoPostagemDTO.class));
        }

        return postagensCadastradasDTO;
    }


    @PutMapping("/{id}")
    @ApiOperation(value="Atualizar postagem")
    public void editarPostagem(@PathVariable Long id, @RequestBody @Valid AtualizarPostagemDTO atualizarPostagemDTO,
                               Authentication authentication) {

        Usuario usuario = conversorAutenticacao.converterAutenticacao(authentication);
        Postagem postagemRecebida = modelMapper.map(atualizarPostagemDTO, Postagem.class);
        postagemService.atualizarPostagem(id, postagemRecebida, usuario);

    }


    @PatchMapping("/{id}")
    @ApiOperation(value="Curtir postagem")
    public RetornoPostagemDTO curtirPostagem(@PathVariable Long id, Authentication authentication) {
        Usuario usuario = conversorAutenticacao.converterAutenticacao(authentication);
        Postagem postagem = postagemService.curtirPostagem(id, usuario);

        return modelMapper.map(postagem, RetornoPostagemDTO.class);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value="Deletar postagem")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirPostagem(@PathVariable Long id, Authentication authentication) {

        Usuario usuario = conversorAutenticacao.converterAutenticacao(authentication);
        postagemService.deletarPostagem(id, usuario);

    }

}
