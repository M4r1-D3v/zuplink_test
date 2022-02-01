package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.enums.Area;
import br.com.zup.gerenciadorDePostagem.enums.Tema;
import br.com.zup.gerenciadorDePostagem.enums.Tipo;
import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface PostagemRepository extends CrudRepository<Postagem,Long> {
    List<Postagem> findAllByArea (Area area);
    List<Postagem> findAllByTipo (Tipo tipo);
    List<Postagem> findAllByTema (Tema tema);
    List<Postagem> findAllByUsuario (Usuario autorPostagem);
    List<Postagem> findAllByLocalDate (LocalDate dataDeCadastro);
    @Query(value = "SELECT * FROM postagens ORDER BY likes DESC", nativeQuery = true)
    List<Postagem> findAllByInteger (Integer likes);

}
