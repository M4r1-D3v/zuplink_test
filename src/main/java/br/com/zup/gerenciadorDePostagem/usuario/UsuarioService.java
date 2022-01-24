package br.com.zup.gerenciadorDePostagem.usuario;

import br.com.zup.gerenciadorDePostagem.exceptions.EmailJaCadstradoException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoCadastrado;
import br.com.zup.gerenciadorDePostagem.usuario.dtos.UsuarioDto;
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

    public Usuario atualizarUsuario(String id, UsuarioDto usuarioDto){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if(usuarioOptional.isEmpty()){
            throw new UsuarioNaoCadastrado ("O usuário não existe, favor Cadastrar");
        }
        Usuario usuarioParaAtualizar = usuarioOptional.get();
        usuarioParaAtualizar.setEmail(usuarioDto.getEmail());

        usuarioRepository.save(usuarioParaAtualizar);


        return usuarioParaAtualizar;
    }

}
