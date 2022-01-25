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

    private String nome;
    private String email;


}
