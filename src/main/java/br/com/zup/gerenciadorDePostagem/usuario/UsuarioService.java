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

        if (usuarioExistente.isPresent()) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        String senhaEscondida = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaEscondida);

        return usuarioRepository.save(usuario);
    }


    public List<Usuario> exibirUsuarios() {

        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
            throw new NaoExistemUsuariosCadastradosException("Não há usuários cadastrados");
        }

        return usuarios;
    }


    public Usuario atualizarUsuario(String email, Usuario usuario) {

        Usuario usuarioParaAtualizar = verificarUsuario(email);

        String senhaEscondida = encoder.encode(usuario.getSenha());

        usuarioParaAtualizar.setEmail(usuario.getEmail());
        usuarioParaAtualizar.setSenha(senhaEscondida);
        usuarioParaAtualizar.setNome(usuario.getNome());

        usuarioRepository.save(usuarioParaAtualizar);


        return usuarioParaAtualizar;
    }


    public void deletarUsuario(String email, String idUsuario) {

        Usuario usuario = verificarUsuario(email);
        validarAutenticidadeUsuario(usuario, idUsuario);
        usuarioRepository.deleteById(usuario.getId());

    }


    private Usuario verificarUsuario(String email) {

        Optional<Usuario> usuarioCadastrado = usuarioRepository.findByEmail(email);

        if (usuarioCadastrado.isPresent()) {
            return usuarioCadastrado.get();
        } else {
            throw new UsuarioNaoCadastradoException("Usuário não cadastrado");
        }

    }


    public void validarAutenticidadeUsuario(Usuario usuarioCadastrado, String id) {

        if (!usuarioCadastrado.getId().equals(id)) {
            throw new UsuarioNaoAutorizadoException("Usuário não autorizado");
        }

    }

}
