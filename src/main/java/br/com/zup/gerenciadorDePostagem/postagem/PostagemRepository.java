package br.com.zup.gerenciadorDePostagem.postagem;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostagemRepository extends CrudRepository<Postagem, Long> {

    @Override
    @Query(value = "SELECT * FROM postagens ORDER BY likes DESC", nativeQuery = true)
    Iterable<Postagem> findAll();

    @Query(value = "SELECT * FROM postagens WHERE area_atuacao=:area ORDER BY likes DESC", nativeQuery = true)
    List<Postagem> area(String area);

    @Query(value = "SELECT * FROM postagens WHERE tipo=:tipo ORDER BY likes DESC", nativeQuery = true)
    List<Postagem> tipo(String tipo);

    @Query(value = "SELECT * FROM postagens WHERE tema=:tema ORDER BY likes DESC", nativeQuery = true)
    List<Postagem> tema(String tema);

    @Query(value = "SELECT * FROM postagens WHERE autor_postagem_id=:autorPostagem ORDER BY likes DESC", nativeQuery = true)
    List<Postagem> autorPostagem(String autorPostagem);

    @Query(value = "SELECT * FROM postagens ORDER BY data_de_cadastro DESC", nativeQuery = true)
    List<Postagem> dataDeCadastroRecente(String dataDeCadastro);

    @Query(value = "SELECT * FROM postagens ORDER BY data_de_cadastro ASC", nativeQuery = true)
    List<Postagem> dataDeCadastroAntiga(String dataDeCadastro);

    @Query(value = "SELECT * FROM postagens ORDER BY likes DESC", nativeQuery = true)
    List<Postagem> like(Integer likes);

    @Query(value = "SELECT * FROM postagens ORDER BY deslikes DESC", nativeQuery = true)
    List<Postagem> deslike(Integer deslikes);

}
