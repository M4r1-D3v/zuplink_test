package br.com.zup.gerenciadorDePostagem.postagem;

import br.com.zup.gerenciadorDePostagem.enums.Area;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostagemRepository extends CrudRepository<Postagem,Long> {
    List<Postagem> findAllByArea (Area area);
}
