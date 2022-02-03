package br.com.zup.gerenciadorDePostagem.curtidas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "likes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long id_postagem;
    private String id_usuario;

    public Like(Long id_postagem, String id_usuario) {
        this.id_postagem = id_postagem;
        this.id_usuario = id_usuario;
    }

}
