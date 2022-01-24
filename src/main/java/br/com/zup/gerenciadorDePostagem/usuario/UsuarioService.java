package br.com.zup.gerenciadorDePostagem.usuario;

import br.com.zup.gerenciadorDePostagem.exceptions.EmailJaCadstradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario (Usuario usuario){
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent()){
            throw new EmailJaCadstradoException();
        }
        return usuarioRepository.save(usuario);
    }

}
