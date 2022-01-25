package br.com.zup.gerenciadorDePostagem.usuario;

import br.com.zup.gerenciadorDePostagem.exceptions.EmailJaCadastradoException;
import br.com.zup.gerenciadorDePostagem.exceptions.IdNaoEncontradoException;
import br.com.zup.gerenciadorDePostagem.exceptions.NaoExistemUsuariosCadastradosException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoCadastrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;


    public Usuario cadastrarUsuario(Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        String senhaEscondida = encoder.encode(usuario.getSenha());

        if (usuarioExistente.isPresent()) {
            throw new EmailJaCadastradoException();
        }

        usuario.setSenha(senhaEscondida);

        return usuarioRepository.save(usuario);
    }


    public Usuario atualizarUsuario(String id, Usuario usuario) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()) {
            throw new UsuarioNaoCadastrado("O usuário não existe, favor Cadastrar");
        }
        Usuario usuarioParaAtualizar = usuarioOptional.get();
        usuarioParaAtualizar.setEmail(usuario.getEmail());
        usuarioParaAtualizar.setSenha(usuario.getSenha());
        usuarioParaAtualizar.setNome(usuario.getNome());

        usuarioRepository.save(usuarioParaAtualizar);


        return usuarioParaAtualizar;
    }

    public List<Usuario> exibirUsuarios() {
        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
            throw new NaoExistemUsuariosCadastradosException();
        }
        return usuarios;
    }

    public void deletarUsuario(String id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new IdNaoEncontradoException("Usuário não encontrado");
        }
    }

}
