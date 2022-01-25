package br.com.zup.gerenciadorDePostagem.postagem;


import br.com.zup.gerenciadorDePostagem.exceptions.NaoExistemPostagensCadastradasException;
import br.com.zup.gerenciadorDePostagem.exceptions.PostagemNaoEncontradaException;
import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PostagemService {

    @Autowired
    private PostagemRepository postagemRepository;


    public Postagem salvarPostagem(Postagem postagem, Usuario autorPostagem) {
        postagem.setLikes(0);
        postagem.setDeslikes(0);
        postagem.setAutorPostagem(autorPostagem);
        postagem.setDataDeCadastro(LocalDate.now());

        return postagemRepository.save(postagem);
    }


    public List<Postagem> exibirPostagens() {
        List<Postagem> postagens = (List<Postagem>) postagemRepository.findAll();
        if (postagens.isEmpty()) {
            throw new NaoExistemPostagensCadastradasException();
        }
        return postagens;
    }

    public void deletarPostagem(Integer id) {
        if (postagemRepository.existsById(id)) {
            postagemRepository.deleteById(id);
        } else {
            throw new PostagemNaoEncontradaException("Postagem não encontrada");
        }

    }

}
