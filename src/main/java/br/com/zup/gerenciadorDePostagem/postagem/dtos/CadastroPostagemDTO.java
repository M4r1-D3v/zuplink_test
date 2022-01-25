package br.com.zup.gerenciadorDePostagem.postagem.dtos;

import br.com.zup.gerenciadorDePostagem.enums.Area;
import br.com.zup.gerenciadorDePostagem.enums.Tema;
import br.com.zup.gerenciadorDePostagem.enums.Tipo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CadastroPostagemDTO {

    @NotBlank(message = "Insira um título")
    @Size(min = 3, message = "O título deve conter no minimo 3 caracteres")
    private String titulo;
    @Max(value = 300, message = "A descrição deve conter no máximo 300 caracteres")
    private String descricao;
    @NotBlank(message = "Insira o link")
    private String link;
    @NotNull(message = "Insira o Tipo de postagem ")
    private Tipo tipo;
    @NotNull(message = "Insira qual o Tema da postagem")
    private Tema tema;
    @NotNull(message = "Insira a area de atuação que está relacionado com a postagem")
    private Area areaAtuacao;

}
