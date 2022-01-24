package br.com.zup.gerenciadorDePostagem.usuario;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario,String> {

    Optional<Usuario>findByEmail(String email);

}
