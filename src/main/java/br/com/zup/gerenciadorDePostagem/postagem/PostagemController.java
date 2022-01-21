package br.com.zup.gerenciadorDePostagem.postagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/postagem")
public class PostagemController {

    @Autowired
    private PostagemService postagemService;

}
