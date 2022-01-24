package br.com.zup.gerenciadorDePostagem.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioLogado  implements UserDetails {

    private String id;
    private String email;
    private String senha;

}
