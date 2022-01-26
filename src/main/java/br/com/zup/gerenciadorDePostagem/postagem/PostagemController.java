package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.config.security.UsuarioLogado;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.PostagemDTO;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.PostagensCadastradasDTO;
import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/postagem")
public class PostagemController {

    @Autowired
    private PostagemService postagemService;
    @Autowired
    private ModelMapper modelMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrarPostagem(@RequestBody @Valid PostagemDTO cadastroPostagemDTO,
                                  Authentication authentication) {

        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
        postagemService.salvarPostagem(modelMapper.map(cadastroPostagemDTO, Postagem.class),
                modelMapper.map(usuarioLogado, Usuario.class));

    }

    @GetMapping
    public List<PostagensCadastradasDTO> exibirPostagensCadastradas() {
        List<PostagensCadastradasDTO> postagensCadastradasDTO = new ArrayList<>();
        for (Postagem postagem : postagemService.exibirPostagens()) {
            postagensCadastradasDTO.add(modelMapper.map(postagem, PostagensCadastradasDTO.class));
        }
        return postagensCadastradasDTO;
    }

    @PutMapping("/{id}")
    public void editarPostagem(@PathVariable Long id, @RequestBody @Valid PostagemDTO postagemDTO,
                               Authentication authentication) {

        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();

        Postagem postagemRecebida = modelMapper.map(postagemDTO, Postagem.class);

        postagemService.atualizarPostagem(id, postagemRecebida, usuarioLogado.getId());

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirPostagem(@PathVariable Long id,Authentication authentication){
        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
        postagemService.deletarPostagem(id,usuarioLogado.getId());
    }

}
