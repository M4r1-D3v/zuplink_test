package br.com.zup.gerenciadorDePostagem.usuario.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class UsuarioSaidaDTO {

    @Id
    private String id;
    private String email;
    private String senha;

}
