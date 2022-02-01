package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.config.security.UsuarioLogado;
import br.com.zup.gerenciadorDePostagem.exceptions.NaoExistemPostagensCadastradasException;
import br.com.zup.gerenciadorDePostagem.exceptions.PostagemNaoEncontradaException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoAutorizadoException;
import br.com.zup.gerenciadorDePostagem.postagem.dtos.AtualizarPostagemDTO;
import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PostagemService {

    @Autowired
    private PostagemRepository postagemRepository;


    public Postagem salvarPostagem(Postagem postagem, Usuario usuario) {

        postagem.setLikes(0);
        postagem.setDeslikes(0);
        postagem.setAutorPostagem(usuario);
        postagem.setDataDeCadastro(LocalDate.now());

        return postagemRepository.save(postagem);
    }

    public List<Postagem> exibirPostagens() {
        List<Postagem> postagens = (List<Postagem>) postagemRepository.findAll();

        if (postagens.isEmpty()) {
            throw new NaoExistemPostagensCadastradasException("Não existem postagens cadastradas!");
        }

        return postagens;
    }

    public Postagem atualizarPostagem(Long idPostagem, Postagem postagemRecebida, Usuario usuarioLogado) {

        Postagem postagemAtualizada = verificarPostagem(idPostagem, usuarioLogado.getId());

        postagemAtualizada.setTitulo(postagemRecebida.getTitulo());
        postagemAtualizada.setDescricao(postagemRecebida.getDescricao());
        postagemAtualizada.setTipo(postagemRecebida.getTipo());
        postagemAtualizada.setTema(postagemRecebida.getTema());
        postagemAtualizada.setAreaAtuacao(postagemRecebida.getAreaAtuacao());
        postagemAtualizada.setDataDeCadastro(LocalDate.now());

        return postagemRepository.save(postagemAtualizada);

    }

    public void deletarPostagem(Long idPostagem, Usuario usuario) {
        verificarPostagem(idPostagem, usuario.getId());

        postagemRepository.deleteById(idPostagem);
    }

    public Postagem verificarPostagem(Long idPostagem) {
        Optional<Postagem> postagemCadastrada = postagemRepository.findById(idPostagem);

        if (postagemCadastrada.isPresent()) {
            return postagemCadastrada.get();
        } else {
            throw new PostagemNaoEncontradaException("Postagem não encontrada");
        }
    }

    public Boolean validarAutenticidade(Usuario usuarioLogado, Postagem postagem) {


        if (usuarioLogado.getId().equals(postagem.getAutorPostagem().getId())) {
            return true;
        }  else{
            throw new UsuarioNaoAutorizadoException("Usuário não autorizado");
        }

    }

}
