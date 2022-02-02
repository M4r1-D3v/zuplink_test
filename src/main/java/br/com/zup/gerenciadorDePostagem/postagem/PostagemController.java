package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.components.ConversorAutenticacao;
import br.com.zup.gerenciadorDePostagem.enums.Area;
import br.com.zup.gerenciadorDePostagem.enums.Tema;
import br.com.zup.gerenciadorDePostagem.enums.Tipo;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.AtualizarPostagemDTO;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.PostagemDTO;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.PostagensCadastradasDTO;
import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrarPostagem(@RequestBody @Valid PostagemDTO cadastroPostagemDTO,
                                  Authentication authentication) {

        Usuario usuario = conversorAutenticacao.converterAutenticacao(authentication);

        postagemService.salvarPostagem(modelMapper.map(cadastroPostagemDTO, Postagem.class), usuario);

    }

    @GetMapping
    public List<PostagensCadastradasDTO> exibirPostagensCadastradas(@RequestParam(required = false)Map<String,String> filtros) {

        List<PostagensCadastradasDTO> postagensCadastradasDTO = new ArrayList<>();

        for (Postagem postagem : postagemService.exibirPostagens(filtros)) {
            postagensCadastradasDTO.add(modelMapper.map(postagem, PostagensCadastradasDTO.class));
        }

        return postagensCadastradasDTO;
    }

    @PutMapping("/{id}")
    public void editarPostagem(@PathVariable Long id, @RequestBody @Valid AtualizarPostagemDTO atualizarPostagemDTO,
                               Authentication authentication) {

        Usuario usuario = conversorAutenticacao.converterAutenticacao(authentication);

        Postagem postagemRecebida = modelMapper.map(atualizarPostagemDTO, Postagem.class);

        postagemService.atualizarPostagem(id, postagemRecebida, usuario);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirPostagem(@PathVariable Long id, Authentication authentication) {
        Usuario usuario = conversorAutenticacao.converterAutenticacao(authentication);

        postagemService.deletarPostagem(id, usuario);
    }

}
