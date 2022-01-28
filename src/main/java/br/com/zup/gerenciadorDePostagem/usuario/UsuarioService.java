package br.com.zup.gerenciadorDePostagem.usuario;

import br.com.zup.gerenciadorDePostagem.exceptions.EmailJaCadastradoException;
import br.com.zup.gerenciadorDePostagem.exceptions.NaoExistemUsuariosCadastradosException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoAutorizadoException;
import br.com.zup.gerenciadorDePostagem.exceptions.UsuarioNaoCadastradoException;
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
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        usuario.setSenha(senhaEscondida);

        return usuarioRepository.save(usuario);
    }


    public Usuario atualizarUsuario(String id, Usuario usuario) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()) {
            throw new UsuarioNaoCadastradoException("O usuário não existe, favor Cadastrar");
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
            throw new NaoExistemUsuariosCadastradosException("Não há usuários cadastrados");
        }
        return usuarios;
    }

    public void deletarUsuario(String email, String idUsuario) {
        Optional<Usuario> usuarioExiste = usuarioRepository.findByEmail(email);

        if (usuarioExiste.isPresent()) {
            if (idUsuario.equals(usuarioExiste.get().getId())){
                usuarioRepository.deleteById(usuarioExiste.get().getId());
            }else{
                throw new UsuarioNaoAutorizadoException("Usuário não autorizado");
            }
        }else {
            throw new UsuarioNaoCadastradoException("Usuário não encontrado");
        }

    }

}
