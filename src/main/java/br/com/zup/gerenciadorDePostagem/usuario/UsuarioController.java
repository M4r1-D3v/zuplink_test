package br.com.zup.gerenciadorDePostagem.usuario;

import br.com.zup.gerenciadorDePostagem.components.ConversorAutenticacao;
import br.com.zup.gerenciadorDePostagem.usuario.dtos.UsuarioDto;
import br.com.zup.gerenciadorDePostagem.usuario.dtos.UsuarioSaidaDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(value = "Gerenciamento de links de postagem.")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ConversorAutenticacao conversorAutenticacao;


    @PostMapping
    @ApiOperation(value="Cadastrar usuário")
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrarUsuario(@RequestBody @Valid UsuarioDto usuarioDTO) {

        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        usuarioService.cadastrarUsuario(usuario);

    }


    @PutMapping
    @ApiOperation(value="Atualizar usuário")
    public void atualizarUsuario(@RequestBody @Valid UsuarioDto usuarioDto, Authentication authentication) {

        Usuario usuario = conversorAutenticacao.converterAutenticacao(authentication);
        usuarioService.atualizarUsuario(usuario.getEmail(), modelMapper.map(usuarioDto, Usuario.class));

    }


    @GetMapping
    @ApiOperation(value="Exibir usuário")
    public List<UsuarioSaidaDTO> exibirUsuariosCadastrados() {

        List<UsuarioSaidaDTO> listaUsuariosSaidaDTO = new ArrayList<>();

        for (Usuario usuario : usuarioService.exibirUsuarios()) {

            listaUsuariosSaidaDTO.add(modelMapper.map(usuario, UsuarioSaidaDTO.class));
        }

        return listaUsuariosSaidaDTO;
    }


    @DeleteMapping
    @ApiOperation(value="Deletar usuário")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarUsuario(@RequestParam String email, Authentication authentication) {

        Usuario usuario = conversorAutenticacao.converterAutenticacao(authentication);
        usuarioService.deletarUsuario(email, usuario.getId());

    }

}
