package br.com.zup.gerenciadorDePostagem.curtidas;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LikeRepository extends CrudRepository<Like, Long> {

    @Query(value = "SELECT * FROM likes WHERE id_postagem=:id_postagem AND id_usuario=:id_usuario", nativeQuery = true)
    Optional<Like> jaCurtiu(Long id_postagem, String id_usuario);

}
