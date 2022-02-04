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

    @Email(regexp = "(^[a-z0-9.-]+[\\w]+@zup\\.com\\.br)",message = "Email fora dos requisitos")
    @NotNull(message = "Insira o e-mail")
    private String email;
    @NotBlank(message = "Insira a senha")
    @Size(min = 4, message = "A senha não pode conter menos de 4 caracteres")
    private String senha;
    @NotBlank(message = "Insira o nome")
    @Size(min = 2, message = "O nome não pode conter menos de 2 caracteres")
    private String nome;

}
