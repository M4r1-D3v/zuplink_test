package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.config.security.UsuarioLogado;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.CadastroPostagemDTO;
import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/postagem")
public class PostagemController {

    @Autowired
    private PostagemService postagemService;
    @Autowired
    private ModelMapper modelMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrarPostagem(@RequestBody @Valid CadastroPostagemDTO cadastroPostagemDTO,
                                  Authentication authentication) {

        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
        postagemService.salvarPostagem(modelMapper.map(cadastroPostagemDTO, Postagem.class),
                modelMapper.map(usuarioLogado, Usuario.class));

    }

}
