package br.com.zup.gerenciadorDePostagem.components;

import br.com.zup.gerenciadorDePostagem.config.security.UsuarioLogado;
import br.com.zup.gerenciadorDePostagem.usuario.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ConversorAutenticacao {

    public Usuario converterAutenticacao(Authentication authentication) {
        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();

        return new Usuario(usuarioLogado.getId(), usuarioLogado.getNome(),
                usuarioLogado.getEmail(), usuarioLogado.getSenha());

    }

}
