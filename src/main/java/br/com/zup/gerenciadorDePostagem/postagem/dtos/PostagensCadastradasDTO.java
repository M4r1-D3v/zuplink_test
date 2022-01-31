package br.com.zup.gerenciadorDePostagem.postagem.dtos;


import br.com.zup.gerenciadorDePostagem.enums.Area;
import br.com.zup.gerenciadorDePostagem.enums.Tema;
import br.com.zup.gerenciadorDePostagem.enums.Tipo;
import br.com.zup.gerenciadorDePostagem.usuario.dtos.ExibirUsuarioPostagemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostagensCadastradasDTO {

    private String titulo;
    private String descricao;
    private String link;
    private Tipo tipo;
    private Tema tema;
    private Area areaAtuacao;
    private ExibirUsuarioPostagemDTO autorPostagem;

}
