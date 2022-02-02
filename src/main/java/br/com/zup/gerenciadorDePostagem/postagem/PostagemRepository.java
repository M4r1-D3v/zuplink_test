package br.com.zup.gerenciadorDePostagem.postagem;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostagemRepository extends CrudRepository<Postagem, Long> {

    @Query(value = "SELECT * FROM postagens WHERE area_atuacao=:area", nativeQuery = true)
    List<Postagem> area(String area);

    @Query(value = "SELECT * FROM postagens WHERE tipo=:tipo", nativeQuery = true)
    List<Postagem> tipo(String tipo);

    @Query(value = "SELECT * FROM postagens WHERE tema=:tema", nativeQuery = true)
    List<Postagem> tema(String tema);

    @Query(value = "SELECT * FROM postagens WHERE autor_postagem_id=:autorPostagem", nativeQuery = true)
    List<Postagem> autorPostagem(String autorPostagem);

    @Query(value = "SELECT * FROM postagens WHERE data_de_cadastro=:dataDeCadastro", nativeQuery = true)
    List<Postagem> dataDeCadastro(String dataDeCadastro);

    @Query(value = "SELECT * FROM postagens ORDER BY likes DESC", nativeQuery = true)
    List<Postagem> like(Integer likes);

    @Query(value = "SELECT * FROM postagens ORDER BY deslikes DESC", nativeQuery = true)
    List<Postagem> deslike(Integer deslikes);

}
