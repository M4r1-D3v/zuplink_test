package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.enums.Area;
import br.com.zup.gerenciadorDePostagem.enums.Tipo;
import br.com.zup.gerenciadorDePostagem.exceptions.NaoExistemPostagensCadastradasException;
import br.com.zup.gerenciadorDePostagem.exceptions.PostagemNaoEncontradaException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoAutorizadoException;
import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
            throw new NaoExistemPostagensCadastradasException("Não existem postagens cadastradas!");
        }

        return postagens;
    }

    public Postagem atualizarPostagem(Long idPostagem, Postagem postagemRecebida, String idUsuario) {

        Postagem postagemAtualizada = verificarPostagem(idPostagem, idUsuario);

        postagemAtualizada.setTitulo(postagemRecebida.getTitulo());
        postagemAtualizada.setDescricao(postagemRecebida.getDescricao());
        postagemAtualizada.setTipo(postagemRecebida.getTipo());
        postagemAtualizada.setTema(postagemRecebida.getTema());
        postagemAtualizada.setAreaAtuacao(postagemRecebida.getAreaAtuacao());
        postagemAtualizada.setDataDeCadastro(LocalDate.now());

        return postagemRepository.save(postagemAtualizada);
    }

    public void deletarPostagem(Long idPostagem, String idUsuario) {
        verificarPostagem(idPostagem, idUsuario);
        postagemRepository.deleteById(idPostagem);
    }

    private Postagem verificarPostagem(Long idPostagem, String idUsuario) {
        Optional<Postagem> postagemCadastrada = postagemRepository.findById(idPostagem);

        if (postagemCadastrada.isPresent()) {
            if (idUsuario.equals(postagemCadastrada.get().getAutorPostagem().getId())) {
                return postagemCadastrada.get();
            } else {
                throw new UsuarioNaoAutorizadoException("Usuário não autorizado");
            }
        }

        throw new PostagemNaoEncontradaException("Postagem não cadastrada");
    }

    public List<Postagem> aplicarFiltroDeBusca (Area area, Tipo tipo) {
        if (area != null) {
            return postagemRepository.findAllByArea(area);
        }
        if (tipo !=null){
            return postagemRepository.findAllByTipo(tipo);
        }
        List<Postagem> postagens = (List<Postagem>) postagemRepository.findAll();
        return exibirPostagens();
    }


}
