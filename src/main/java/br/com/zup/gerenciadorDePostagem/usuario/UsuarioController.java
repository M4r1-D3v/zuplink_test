package br.com.zup.gerenciadorDePostagem.usuario;

import br.com.zup.gerenciadorDePostagem.config.security.UsuarioLogado;
import br.com.zup.gerenciadorDePostagem.usuario.dtos.UsuarioDto;
import br.com.zup.gerenciadorDePostagem.usuario.dtos.UsuarioSaidaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrarUsuario(@RequestBody @Valid UsuarioDto usuarioDTO) {
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        usuarioService.cadastrarUsuario(usuario);
    }

    @PutMapping
    public void atualizarUsuario(@RequestBody UsuarioDto usuarioDto, Authentication authentication) {
        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
        Usuario usuario = usuarioService.atualizarUsuario(usuarioLogado.getId(), modelMapper.map(usuarioDto,
                Usuario.class));

    }

    @GetMapping
    public List<UsuarioSaidaDTO> exibirUsuariosCadastrados() {
        List<UsuarioSaidaDTO> listaUsuariosSaidaDTO = new ArrayList<>();
        for (Usuario usuario : usuarioService.exibirUsuarios()) {

            listaUsuariosSaidaDTO.add(modelMapper.map(usuario, UsuarioSaidaDTO.class));
        }
        return listaUsuariosSaidaDTO;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarUsuario (@RequestParam String email, Authentication authentication){
        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
        usuarioService.deletarUsuario(email, usuarioLogado.getId());
    }

}
