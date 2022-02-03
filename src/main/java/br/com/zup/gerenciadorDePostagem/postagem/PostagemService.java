package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.enums.Area;
import br.com.zup.gerenciadorDePostagem.enums.Tema;
import br.com.zup.gerenciadorDePostagem.enums.Tipo;
import br.com.zup.gerenciadorDePostagem.exceptions.NaoExistemPostagensCadastradasException;
import br.com.zup.gerenciadorDePostagem.exceptions.PostagemNaoEncontradaException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoAutorizadoException;
import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

    public List<Postagem> exibirPostagens(Map<String,String> filtros) {

        List<Postagem> postagens = (List<Postagem>) postagemRepository.findAll();

        if (postagens.isEmpty()) {
            throw new NaoExistemPostagensCadastradasException("Não existem postagens cadastradas!");
        }

        return aplicarFiltroDeBusca(postagens, filtros);
    }

    public List<Postagem> aplicarFiltroDeBusca(List<Postagem> listaPostagens,Map<String,String> filtros) {

        if (filtros.get("area") != null){
            return postagemRepository.area(filtros.get("area"));
        }else if(filtros.get("tipo") != null){
            return postagemRepository.tipo(filtros.get("tipo"));
        }else if(filtros.get("tema") != null){
            return postagemRepository.tema(filtros.get("tema"));
        }else if(filtros.get("autorPostagem") != null){
            return postagemRepository.autorPostagem(filtros.get("autorPostagem"));
        }else if(filtros.get("dataDeCadastro") != null){
            return postagemRepository.dataDeCadastro(filtros.get("dataDeCadastro"));
        }else if(filtros.get("likes") != null){
            return postagemRepository.like(Integer.parseInt(filtros.get("likes")));
        }else if(filtros.get("deslikes") != null){
            return postagemRepository.deslike(Integer.parseInt(filtros.get("deslikes")));
        }

        return listaPostagens;
    }

    public Postagem atualizarPostagem(Long idPostagem, Postagem postagemRecebida, Usuario usuarioLogado) {

        Postagem postagemAtualizada = verificarPostagem(idPostagem);
        validarAutenticidade(usuarioLogado,postagemAtualizada);

        postagemAtualizada.setTitulo(postagemRecebida.getTitulo());
        postagemAtualizada.setDescricao(postagemRecebida.getDescricao());
        postagemAtualizada.setTipo(postagemRecebida.getTipo());
        postagemAtualizada.setTema(postagemRecebida.getTema());
        postagemAtualizada.setAreaAtuacao(postagemRecebida.getAreaAtuacao());
        postagemAtualizada.setDataDeCadastro(LocalDate.now());

        return postagemRepository.save(postagemAtualizada);

    }

    public void deletarPostagem(Long idPostagem, Usuario usuario) {
        Postagem postagem = verificarPostagem(idPostagem);
        validarAutenticidade(usuario, postagem);

        postagemRepository.deleteById(postagem.getId());

    }

    public Postagem verificarPostagem(Long idPostagem) {
        Optional<Postagem> postagemCadastrada = postagemRepository.findById(idPostagem);

        if (postagemCadastrada.isPresent()) {
            return postagemCadastrada.get();
        } else {
            throw new PostagemNaoEncontradaException("Postagem não encontrada");
        }
    }


    public void validarAutenticidade(Usuario usuarioLogado, Postagem postagem) {

        if (!usuarioLogado.getId().equals(postagem.getAutorPostagem().getId())) {
            throw new UsuarioNaoAutorizadoException("Usuário não autorizado");
        }

    }

}
