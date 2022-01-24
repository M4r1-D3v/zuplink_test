package br.com.zup.gerenciadorDePostagem.usuario;

import br.com.zup.gerenciadorDePostagem.usuario.dtos.UsuarioDto;
import br.com.zup.gerenciadorDePostagem.usuario.dtos.UsuarioSaidaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    public UsuarioSaidaDTO atualizarUsuario (@PathVariable String id, @PathVariable @Valid UsuarioDto usuarioDto){
        Usuario usuario = usuarioService.atualizarUsuario(id, usuarioDto);

        return modelMapper.map(usuario, UsuarioSaidaDTO.class);
    }

}
