package br.com.zup.gerenciadorDePostagem.usuario.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class UsuarioDto {

    @Email
    @NotNull(message = "Insira o e-mail")
    private String email;
    @NotBlank
    @Size(min = 4, message = "A senha não pode conter menos de 4 caracteres")
    private String senha;

}
